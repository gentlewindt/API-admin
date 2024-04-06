package com.gentlewind.apiinterface;

import com.gentlewind.sdk.client.ApiClient;
import com.gentlewind.sdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApiInterfaceApplicationTests {

    // 注入一个名为ApiClient的Bean
    @Resource
    private ApiClient apiClient;

    // 测试方法
    @Test
    void contextLoads() {
//        // 调用ApiClient的getNameByGet方法，并传入参数"gentlewind"
//        String result1 = ApiClient.getNameByGet("gentlewind");
//
//        // 调用ApiClient的getUserBy
//         User user = new User();
//         user.setUsername("tkf");
//         String result2 = ApiClient.getUserNameByPost(user);
//
//        System.out.println(result1);
//        System.out.println(result2);
//
    }

}
