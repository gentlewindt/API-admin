package com.gentlewind.sdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import com.gentlewind.sdk.model.User;


import java.util.HashMap;
import java.util.Map;

import static com.gentlewind.sdk.utils.SignUtils.genSign;

/**
 * 调用第三方接口的客户端
 *
 * @author gentlewind
 */
public class ApiClient {
    private static String accessKey;
    private static String secretKey;

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public static String getNameByGet(String name) {

        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result= HttpUtil.get("http://localhost:8123/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    public static String getNameByPost(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        // 使用HttpUtil工具发起post请求，并获取服务器的返回结果
        String result= HttpUtil.post("http://localhost:8123/api/name/username", paramMap);
        System.out.println(result);
        return result;
    }

    /**
     * 获取请求头的哈希映射
     * @param body 请求体内容
     * @return 包含请求头和签名的哈希映射
     */
    private static Map<String,String> getHeaderMap(String body){
        // 创建一个hashmap对象
         Map<String,String> hashMap = new HashMap<>();
        // 存入accessKey和secretKey
        hashMap.put("accessKey",accessKey);
        // hashMap.put("secretKey","asdfqwer"); 不能直接发送秘钥！！

        // 存入随机数（生成一个100位的随机数）
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        // 存入请求体
        hashMap.put("body",body);
        // 存入时间戳
        hashMap.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
        // 生成签名
        hashMap.put("sign",genSign(body,secretKey));
        //返回构造的请求头hashmap
        return hashMap;
    }


    /**
     * 通过Post请求获取用户名
     * @param user  用户对象
     * @return 从服务器获取的用户名
     */
    public static String getUserNameByPost(User user) {
        // 将user对象转为json字符串
        String json = JSONUtil.toJsonStr(user);
        // 发送post请求，使用HttpResponse对象来接收响应，可以更好的操作响应，比如获取状态码，处理响应头等
        HttpResponse httpResponse = HttpRequest.post("http://localhost:8123/api/name/user")
                .addHeaders(getHeaderMap(json)) // 添加上面写的请求头
                .body(json) // 设置请求体为json字符串
                .execute(); // 执行http请求,调用 execute() 方法可以将 HttpRequest 对象发送给服务器，并获取到服务器返回的响应数据（这是一个链式调用）
                            // 和HttpRequest对比：HttpRequest 对象是用于构建 HTTP 请求的对象，它包含了请求的各种属性和参数，但不包含实际的请求执行结果。
        System.out.println(httpResponse.getStatus()); // 打印状态码
        String result = httpResponse.body(); // 打印响应体内容
        System.out.println(result);
        return result;
    }
}