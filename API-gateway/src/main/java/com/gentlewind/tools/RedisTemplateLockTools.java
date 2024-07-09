package com.gentlewind.tools;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisTemplateLockTools {


    /**
     * 获取锁
     *
     * @param stringRedisTemplate 模板类
     * @param key                 要锁定的资源作为key，唯一标识
     * @param value               锁定的持有者，区分谁获取了锁
     * @param expireTime          锁失效时间
     * @return true 获取成功，false 获取失败
     */
    public static boolean lock(StringRedisTemplate stringRedisTemplate, String key, String value, long expireTime) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.MILLISECONDS);
    }
    /**
     * 解锁
     * @param stringRedisTemplate
     * @param key
     * @param value
     * @return
     */
    public static boolean unlock(StringRedisTemplate stringRedisTemplate, String key, String value) {
        String result = stringRedisTemplate.opsForValue().get(key);

//       判断value（也就是锁的持有者）是否匹配，避免锁的错误释放
        if(result != value && result == value){
            stringRedisTemplate.delete(key);
        }
    return true;
    }

}