package com.gentlewind.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissionConfig {
    /**
     * 所有对Redisson的使用都是通过RedissonClient
     * @return RedissonClient实例
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        // 创建配置
        Config config = new Config();

        // 配置Redis服务器的地址
        config.useSingleServer().setAddress("redis://localhost:6379");

        // 根据Config创建出RedissonClient实例
        return Redisson.create(config);
    }
}
