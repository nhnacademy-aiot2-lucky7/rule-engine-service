package com.nhnacademy.ruleengineservice.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.nhnacademy.ruleengineservice.redis.provider.RedisProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final RedisProvider redisEnvProvider;

    /**
     * Redis 서버와의 연결을 위한 LettuceConnectionFactory 빈을 생성합니다.
     * <p>
     * Redis 서버의 호스트, 포트, 비밀번호 등의 정보를 RedisProvider를 통해 가져와 설정합니다.
     * </p>
     *
     * @return LettuceConnectionFactory 객체
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisEnvProvider.getRedisHost());
        config.setPort(redisEnvProvider.getRedisPort());
        config.setPassword(RedisPassword.of(redisEnvProvider.getRedisPassword()));
        config.setDatabase(redisEnvProvider.getRedisDatabase());
        return new LettuceConnectionFactory(config);
    }

    /**
     * RedisTemplate 빈을 생성합니다.
     * <p>
     * RedisTemplate은 Redis와의 데이터 교환을 위한 템플릿으로, 키와 값에 대한 직렬화 방식으로 StringRedisSerializer를 사용합니다.
     * </p>
     *
     * @param connectionFactory LettuceConnectionFactory 객체
     * @return RedisTemplate<String, Object> 객체
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
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
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        // Value 직렬화기 설정
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}