/*
 * @Date: 2021-09-15 14:19:21
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-11 18:16:38
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\config\RedisConfig.java
 */
package com.kaoqin.stzb.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
 
 
@Configuration
@EnableCaching
public class RedisConfig  {
    private static final StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        //key序列化方式
        template.setKeySerializer(STRING_SERIALIZER);
        //value序列化
        template.setValueSerializer(STRING_SERIALIZER);
        //key hashmap序列化
        template.setHashKeySerializer(STRING_SERIALIZER);
        //value hashmap序列化
        template.setHashValueSerializer(STRING_SERIALIZER);
        return template;
    }
 
}