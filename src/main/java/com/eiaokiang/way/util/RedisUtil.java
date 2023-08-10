package com.eiaokiang.way.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Eiaokiang
 * @Description:
 * @Date: Created in 11:14 2023/8/10
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * redis存入数据
     *
     * @param key      键名
     * @param value    值
     * @param time     保存时间
     * @param timeUnit 时间单位
     */
    public void saveValue(String key, Object value, int time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    /**
     * 获取redis中的值
     *
     * @param key 键名
     */
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

}
