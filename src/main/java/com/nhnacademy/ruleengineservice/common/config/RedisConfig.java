package com.nhnacademy.ruleengineservice.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisConfig 클래스는 Spring Data Redis를 설정하는 클래스입니다.
 * <p>
 * Redis 연결 설정을 위한 LettuceConnectionFactory를 제공하며, RedisTemplate을 생성하여 Redis 서버와의 상호작용을 가능하게 합니다.
 * 이 클래스는 Redis 환경 설정을 로드하기 위해 RedisProvider를 사용합니다.
 * </p>
 */
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

        // 문자열 Key 직렬화
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        // ObjectMapper 설정 (안전하게 타입 제한)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        BasicPolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.nhnacademy.ruleengineservice") // 안전하게 패키지 제한
                .build();

        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);

        // 직렬화기 생성 시 ObjectMapper 전달
        Jackson2JsonRedisSerializer<SensorRule> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, SensorRule.class);

        // Value 직렬화기 설정
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String, SensorRule> sensorRuleRedisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, SensorRule> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key는 String
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value는 SensorRule
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        BasicPolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.nhnacademy.ruleengineservice") // SensorRule 패키지 포함
                .build();

        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);

        Jackson2JsonRedisSerializer<SensorRule> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, SensorRule.class);

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

}