package com.gentlewind.sdk;

import com.gentlewind.sdk.client.ApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration                          // 将改类标记为一个配置类，告诉Spring这是一个用于配置的类
@ConfigurationProperties("api.client")  // 读取application.yml的配置，将读到的配置设置到这里的属性中 ; 给所有的配置加上前缀"api.client"
@Data                                   // lombok注解，自动生成getters等方法
@ComponentScan                          // 自动扫描组件，使得Spring自动注册响应的Bean
public class ApiClientSdkConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public ApiClient myClient() {
        return new ApiClient(accessKey, secretKey);
    }
}
