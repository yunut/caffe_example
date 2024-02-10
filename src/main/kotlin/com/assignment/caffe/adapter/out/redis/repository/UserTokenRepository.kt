package com.assignment.caffe.adapter.out.redis.repository

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class UserTokenRepository(
    @Qualifier("redisTemplate") private val redisTemplate: RedisTemplate<String, String>,
) {
    fun save(key: String, value: String) {
        redisTemplate.opsForValue().set(key, value)
    }

    fun find(key: String): String? {
        return redisTemplate.opsForValue().get(key)
    }

    fun delete(key: String) {
        redisTemplate.delete(key)
    }

    fun exists(key: String): Boolean {
        return redisTemplate.hasKey(key)
    }

    fun expire(key: String, expire: Long) {
        redisTemplate.expire(key, expire, TimeUnit.HOURS)
    }

    // 레디스 만료시간 설정후 save
    fun saveWithExpire(key: String, value: String, expire: Long) {
        redisTemplate.opsForValue().set(key, value)
        redisTemplate.expire(key, expire, TimeUnit.HOURS)
    }
}
