package com.assignment.caffe.adapter.out.redis

import com.assignment.caffe.adapter.out.redis.repository.UserTokenRepository
import com.assignment.caffe.application.domain.model.UserBlacklistToken
import com.assignment.caffe.application.domain.model.UserRefreshToken
import com.assignment.caffe.application.port.out.UserTokenPort
import org.springframework.stereotype.Component

@Component
class UserTokenAdapter(
    private val userTokenRepository: UserTokenRepository,
) : UserTokenPort {
    override fun insertRefreshToken(userRefreshToken: UserRefreshToken, expireHour: Long) {
        userTokenRepository.saveWithExpire(userRefreshToken.key, userRefreshToken.refreshToken, expireHour)
    }

    override fun findRefreshTokenByUserId(id: String): UserRefreshToken? {
        val refreshToken = userTokenRepository.find("${UserRefreshToken.prefix}$id")
        return refreshToken?.let { UserRefreshToken.of(id, it) }
    }

    override fun deleteRefreshTokenByUserId(id: String) {
        userTokenRepository.delete("${UserRefreshToken.prefix}$id")
    }

    override fun insertBlackListToken(userBlacklistToken: UserBlacklistToken, expireHour: Long) {
        userTokenRepository.saveWithExpire(userBlacklistToken.key, userBlacklistToken.accessToken, expireHour)
    }

    override fun findBlackListToken(token: String): Boolean {
        return userTokenRepository.exists("${UserBlacklistToken.prefix}$token")
    }
}
