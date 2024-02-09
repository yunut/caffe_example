package com.assignment.caffe.application.port.out

import com.assignment.caffe.application.domain.model.UserToken

interface UserTokenPort {

    fun findByUserId(id: Int): UserToken?

    fun insertToken(userToken: UserToken)

    fun findByUserIdAndReissueCountLessThan(id: Int, count: Long): UserToken?

    fun deleteTokenByUserId(id: Int)
}
