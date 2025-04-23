package com.nhnacademy.ruleengineservice.redis.provider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

class RedisProviderTest {

    private RedisProvider redisProvider;

    @BeforeEach
    void setUp() {
        Environment mockEnv = Mockito.mock(Environment.class);
        redisProvider = new RedisProvider(mockEnv);
    }

    @Test
    @DisplayName(".env 값 일치 확인")
    void testGetRedisHost_success() {

        Assertions.assertEquals("localhost", redisProvider.getRedisHost());
        Assertions.assertEquals("password123!@#", redisProvider.getRedisPassword());
        Assertions.assertEquals(6379, redisProvider.getRedisPort());
        Assertions.assertEquals(1, redisProvider.getRedisDatabase());
    }
}
