package com.assignment.caffe.adapter.out.persistence

import com.assignment.caffe.adapter.out.persistence.repository.UserTokenRepository
import com.assignment.caffe.application.domain.model.UserToken
import com.assignment.caffe.application.port.out.UserTokenPort
import org.springframework.stereotype.Component

@Component
class UserTokenAdapter(
    private val userRefreshTokenRepository: UserTokenRepository,
) : UserTokenPort {
    override fun findByUserId(id: Int): UserToken? {
        return userRefreshTokenRepository.findByUserId(id)
    }

    override fun insertToken(userToken: UserToken) {
        userRefreshTokenRepository.save(userToken)
    }

    override fun findByUserIdAndReissueCountLessThan(id: Int, count: Long): UserToken? {
        return userRefreshTokenRepository.findByUserIdAndReissueCountLessThan(id, count)
    }

    override fun deleteTokenByUserId(id: Int) {
        userRefreshTokenRepository.deleteByUserId(id)
    }
}
