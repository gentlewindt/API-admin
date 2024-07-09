package com.gentlewind.apigateway;

import com.gentlewind.sdk.utils.SignUtils;
import com.gentlewind.tools.RedisTemplateLockTools;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    //    引入StringRedisTemplate，用于操作Redis
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplateLockTools redisTemplateLockTools;

    // 白名单，只允许特定的调用（更安全）
    private static final List<String> IP_WHITE_IP = Arrays.asList("127.0.0.1");

    @Override //在filter中编写业务逻辑
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        1. 用户发送请求到 API 网关
//        2. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + request.getPath().value()); // .value()将请求路径转换为String类型
        log.info("请求路径：" + request.getMethod());
        log.info("请求路径：" + request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString(); // 拿到String类型的本地地址
        log.info("请求来源路径：" + sourceAddress);
        log.info("请求来源路径：" + request.getRemoteAddress());


//        3. （黑白名单）
        // 1) 首先在上面写一个全局常量，使用白名单。通常建议在权限管理中尽量使用白名单，少用黑名单。
        // 2) 如果白名单不包含指定地址，则拒绝该请求
        // 通过exchange获取响应对象，从而控制该响应，直接设置状态码为403（禁止访问），拦截该响应
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_IP.contains(sourceAddress)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            // 返回处理完成的响应
            return response.setComplete(); // 返回了一个 Mono，它并不包含响应参数。相当于我们告诉程序，请求处理完成了，不需要再执行其他操作了
        }


//        4. 用户鉴权（判断 ak、sk 是否合法）

        // 从请求头中获取参数
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String sign = headers.getFirst("sign");
        String timestamp = headers.getFirst("timestamp");
        String body = headers.getFirst("body");

        // todo : 去数据库查询是否已分配给用户
        if (!accessKey.equals("gentlewind")) {
            return handleNoAuth(response);
        }
        // 校验一下随机数，直接判断一下nonce是否大于10000
        if (Long.parseLong(nonce) > 10000) { // 将字符串转换为long类型
            return handleNoAuth(response);
        }
        // 请求的时间间隔不能超过5分钟
        Long currentTime = System.currentTimeMillis() / 1000;
        Long formerTime = Long.parseLong(timestamp);
        Long FIVE_MINUTES = 60 * 5L;
        if (currentTime - formerTime >= FIVE_MINUTES) {
            return handleNoAuth(response);
        }


        //todo 实际情况中是从数据库中查出 secretKey
        String serverSign = SignUtils.genSign(body, "asdfqwer");
        // 如果生成的签名不一致，则抛出异常，并提示"无权限"
        if (!serverSign.equals(sign)) {
            return handleNoAuth(response);
        }

//        5.防止重复提交，获取锁
//        使用nonce作为key，因为nonce设计的初衷就是具备唯一性，以此保证通行的安全性。
        boolean success = redisTemplateLockTools.lock(stringRedisTemplate, nonce, accessKey, 100000);
        if (!success) {
            log.error("重复请求，拒绝访问");
            return handleNoAuth(response);
        }

//        5. 请求的模拟接口是否存在？
        // todo: 从数据库中查询模拟接口是否存在，以及请求方法是否匹配（还可以校验请求参数）

//        6. 请求转发，调用模拟接口
        Mono<Void> filter = chain.filter(exchange);


//       释放锁
        redisTemplateLockTools.unlock(stringRedisTemplate, nonce, accessKey);


//        7. 响应日志
        // 调用成功后输入一个响应日志
        return handelResponse(exchange, chain);


    }


    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handelResponse(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            // 获取原始的响应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 获取数据缓冲工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 获取响应的状态码
            HttpStatus statusCode = originalResponse.getStatusCode();
            // 判断状态码200 OK
            if (statusCode == HttpStatus.OK) {
                // 创建一个装饰后的响应对象（开始增强能力）
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    // 重写writeWith方法，用户处理响应体的数据
                    // 这段方法就是只要当我们的模拟接口调用完成之后，等他返回结果，就会调用writeWith方法，我们就能根据响应结果做一些自己的处理
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        // 判断响应体是否是Flux类型
                        // (这里就理解为它在拼接字符串,它把缓冲区的数据取出来，一点一点拼接好)
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 返回一个处理后的响应体
                            // 拼接字符串，把缓冲区的数据取出来，一点一点拼接好
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // 7. 调用成功，接口调用次数 + 1 invokeCount
                                // 读取响应体的内容并转换为字节数组
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
//                                sb2.append("<--- {} {} \n");
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                sb2.append(data);
                                // 打印日志
                                log.info("响应结果：" + data);
                                // 将处理后的内容重新包装成DataBuffer并返回
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            // 9. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 对于200 OK的请求,将装饰后的响应对象传递给下一个过滤器链,并继续处理(设置repsonse对象为装饰过的)
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            // 对于非200 OK的请求，直接返回，进行降级处理
            return chain.filter(exchange);//降级处理返回数据
        } catch (Exception e) {
            // 处理异常情况，记录错误日志
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }


    @Override
    public int getOrder() {
        return -1;
    }

    // 定义一个全局设置修改状态码的方法
    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        // 返回处理完成的响应
        return response.setComplete();

    }

    // 如果调用失败，则返回一个500（调用失败）
    public Mono<Void> handlInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }


}