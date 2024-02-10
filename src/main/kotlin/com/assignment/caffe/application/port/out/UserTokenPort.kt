package com.assignment.caffe.application.port.out

import com.assignment.caffe.application.domain.model.UserBlacklistToken
import com.assignment.caffe.application.domain.model.UserRefreshToken

interface UserTokenPort {

    fun insertRefreshToken(userRefreshToken: UserRefreshToken, expireHour: Long)

    fun findRefreshTokenByUserId(id: Int): UserRefreshToken?

    fun deleteRefreshTokenByUserId(id: Int)

    fun insertBlackListToken(userBlacklistToken: UserBlacklistToken, expireHour: Long)

    fun findBlackListToken(token: String): Boolean
}
