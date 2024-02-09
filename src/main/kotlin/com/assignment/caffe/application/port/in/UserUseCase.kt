package com.assignment.caffe.application.port.`in`

import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.domain.model.UserToken
import com.assignment.caffe.application.port.`in`.query.UserSignInQuery
import com.assignment.caffe.application.port.`in`.query.UserSignUpQuery

interface UserUseCase {
    @Throws(ConflictException::class)
    fun signUp(userSignUpQuery: UserSignUpQuery)

    fun signIn(userSignInQuery: UserSignInQuery): UserToken
}