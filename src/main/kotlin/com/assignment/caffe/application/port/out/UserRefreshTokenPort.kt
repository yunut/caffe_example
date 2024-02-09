package com.assignment.caffe.application.port.out

import com.assignment.caffe.application.domain.model.UserRefreshToken

interface UserRefreshTokenPort {

    fun findByUserId(id: Int): UserRefreshToken?

    fun insertRefreshToken(userRefreshToken: UserRefreshToken)

    fun findByUserIdAndReissueCountLessThan(id: Int, count: Long): UserRefreshToken?
}
