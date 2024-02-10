package com.assignment.caffe.application.port.out

import com.assignment.caffe.application.domain.model.UserBlacklistToken
import com.assignment.caffe.application.domain.model.UserRefreshToken

interface UserTokenPort {

    fun insertRefreshToken(userRefreshToken: UserRefreshToken, expireHour: Long)

    fun findRefreshTokenByUserId(id: String): UserRefreshToken?

    fun deleteRefreshTokenByUserId(id: String)

    fun insertBlackListToken(userBlacklistToken: UserBlacklistToken, expireHour: Long)

    fun findBlackListToken(token: String): Boolean
}
