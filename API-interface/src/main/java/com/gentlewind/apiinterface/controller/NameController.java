package com.gentlewind.apiinterface.controller;
// SDK
import com.gentlewind.sdk.model.User;
import com.gentlewind.sdk.utils.SignUtils;

import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


/**
 * 获取名字API
 *
 * @author gentlewind
 */

@RestController
//一个组合注解，它包含了 @Controller 和 @ResponseBody 注解。@Controller 用于标识一个类是控制器，而 @ResponseBody 则用于将方法的返回值直接写入 HTTP 响应体中。
@RequestMapping("/name")  // 用于指定处理请求的的根路径为/name，该控制器下的所有请求都会以/name开头
public class NameController {

    @GetMapping("/get") // 路径为根路径/name/，请求方式为GET
    public String getNameByGet(String name) {
        return "Get hello" + name;
    }

    @PostMapping("/username") // 路径为根路径/name/uername，请求方式为POST
    public String getNameByPost(@RequestParam String name) { // 通过@RequestParam注解获取请求参数
        return "Post hello" + name;
    }

    @PostMapping("/user")
    public String getNameByPost(@RequestBody User username, HttpServletRequest request) { // 通过@RequestBody注解获取请求体中的参数
        // 从请求头中获取参数
        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String sign = request.getHeader("sign");
        String timestamp = request.getHeader("timestamp");
        String body = request.getHeader("body");

        // todo : 去数据库查询是否已分配给用户
        if (!accessKey.equals("gentlewind")) {
            throw new RuntimeException("无权限访问");
        }
        // 校验一下随机数，直接判断一下nonce是否大于10000
        if (Long.parseLong(nonce) > 10000) { // 将字符串转换为long类型
            throw new RuntimeException("无权限访问");
        }
        //todo : 时间和当前时间不能超过5分钟
        //if (timestamp) {}

        //todo 实际情况中是从数据库中查出 secretKey
        String serverSign = SignUtils.genSign(body,"asdfqwer");
        // 如果生成的签名不一致，则抛出异常，并提示"无权限"
        if(!serverSign.equals(sign)) {
            throw new RuntimeException("无权限访问");
        }

        // 如果权限校验通过，返回"POST 用户名是"+ 用户名
        String result = "POST 用户名是" + username.getUsername();
        return result;
    }
}
