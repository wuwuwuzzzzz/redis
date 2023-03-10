package com.example.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 *
 * @author wxz
 * @date 17:32 2022/12/21
 */
@Configuration
public class RedisConfig {

    /**
     *
     * @author wxz
     * @date 14:44 2022/12/23
     * @param lettuceConnectionFactory redis基本配置
     * @return org.springframework.data.redis.core.RedisTemplate<java.lang.String,java.io.Serializable>
     */
    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    /**
     *
     * @author wxz
     * @date 20:16 2022/12/24
     * @return org.redisson.Redisson
     */
    @Bean
    public Redisson redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://10.211.55.5:6379").setDatabase(0);
        return (Redisson) Redisson.create(config);
    }
}
