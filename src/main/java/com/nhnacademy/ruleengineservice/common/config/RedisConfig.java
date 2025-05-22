package com.nhnacademy.ruleengineservice.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
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

    /**
     * Redis 연결 팩토리 설정
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);

        if (password != null && !password.isBlank()) {
            config.setPassword(RedisPassword.of(password));
        } else {
            log.warn("Redis 비밀번호가 설정되지 않았습니다. 인증 없이 연결합니다.");
        }

        config.setDatabase(database);

        return new LettuceConnectionFactory(config);
    }

    /**
     * RedisTemplate 설정
     */
    @Bean
    public RedisTemplate<String, SensorRule> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, SensorRule> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key와 HashKey는 문자열 직렬화 사용
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        // ObjectMapper 세팅
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        BasicPolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.nhnacademy.ruleengineservice")
                .build();

        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);

        Jackson2JsonRedisSerializer<SensorRule> sensorRuleSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, SensorRule.class);

        // Value와 HashValue 직렬화기에 설정
        template.setValueSerializer(sensorRuleSerializer);
        template.setHashValueSerializer(sensorRuleSerializer);

        template.afterPropertiesSet();
        return template;
    }

}
