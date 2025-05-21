package com.nhnacademy.ruleengineservice.common.config;

import com.nhnacademy.ruleengineservice.common.exception.InvalidRedisPasswordException;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.database}")
    private int database;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);

        if (password == null) {
            throw new InvalidRedisPasswordException("Redis 비밀번호가 비어 있습니다.");
        }

        if (!password.isBlank()) {
            config.setPassword(RedisPassword.of(password));
        } else {
            log.warn("Redis password is blank. Connecting without authentication.");
        }

        config.setDatabase(database);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, SensorRule> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, SensorRule> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key 직렬화기
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        // Value 직렬화기 (Jackson 3.x 호환)
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();

        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }

}
