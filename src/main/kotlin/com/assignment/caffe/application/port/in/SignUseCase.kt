package com.assignment.caffe.application.port.`in`

import com.assignment.caffe.application.domain.dto.UserTokenDto
import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.domain.exception.NotMatchException
import com.assignment.caffe.application.port.`in`.query.SignInQuery
import com.assignment.caffe.application.port.`in`.query.SignUpQuery

interface SignUseCase {
    @Throws(ConflictException::class)
    fun signUp(signUpQuery: SignUpQuery)

    @Throws(NotMatchException::class)
    fun signIn(signInQuery: SignInQuery): UserTokenDto

    fun signOut(userId: String, accessToken: String)
}
