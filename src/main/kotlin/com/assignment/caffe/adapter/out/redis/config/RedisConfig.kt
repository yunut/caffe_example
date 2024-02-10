package com.assignment.caffe.adapter.out.redis.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
class RedisConfig(
    val factory: LettuceConnectionFactory,
) {

    @Bean("redisTemplate")
    fun redisTemplate(): RedisTemplate<String, String> {
        val keySerializer = Jackson2JsonRedisSerializer(String::class.java)
        val valueSerializer = Jackson2JsonRedisSerializer(String::class.java) // Serializer 등록해주지 않으면 이진코드로 저장되기 때문에
        val redisTemplate = RedisTemplate<String, String>()
        redisTemplate.setConnectionFactory(factory)
        redisTemplate.setEnableTransactionSupport(true)
        redisTemplate.keySerializer = keySerializer
        redisTemplate.valueSerializer = valueSerializer

        return redisTemplate
    }

    @Bean
    fun transactionManager(): PlatformTransactionManager {
        return JpaTransactionManager()
    }
}
