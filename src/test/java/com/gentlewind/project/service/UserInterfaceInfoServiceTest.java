package com.gentlewind.project.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class) //注意junit4这个如果不加会显示userInterfaceInfoService为null
public class UserInterfaceInfoServiceTest {

    @Resource
     private UserInterfaceInfoService userInterfaceInfoService;

    @Test
    public void invokeCount() {

        // 调用userInterfaceService的invokeCount方法，并传入两个从参数（1L，1L)
        boolean b =  userInterfaceInfoService.invokeCount(1L,1L);
        // 设置断言，断言b的值为true，即期望测试用例invokeCount方法返回true
        Assertions.assertTrue(b);

    }
}