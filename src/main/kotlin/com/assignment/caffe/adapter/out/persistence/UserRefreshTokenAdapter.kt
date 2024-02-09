package com.assignment.caffe.adapter.out.persistence

import com.assignment.caffe.adapter.out.persistence.repository.UserRefreshTokenRepository
import com.assignment.caffe.application.domain.model.UserRefreshToken
import com.assignment.caffe.application.port.out.UserRefreshTokenPort
import org.springframework.stereotype.Component

@Component
class UserRefreshTokenAdapter(
    private val userRefreshTokenRepository: UserRefreshTokenRepository,
) : UserRefreshTokenPort {
    override fun findByUserId(id: Int): UserRefreshToken? {
        return userRefreshTokenRepository.findByUserId(id)
    }

    override fun insertRefreshToken(userRefreshToken: UserRefreshToken) {
        userRefreshTokenRepository.save(userRefreshToken)
    }

    override fun findByUserIdAndReissueCountLessThan(id: Int, count: Long): UserRefreshToken? {
        return userRefreshTokenRepository.findByUserIdAndReissueCountLessThan(id, count)
    }
}
