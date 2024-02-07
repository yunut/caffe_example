package com.assignment.caffe.application.domain.service

import com.assignment.caffe.application.port.`in`.UserUseCase
import com.assignment.caffe.application.port.`in`.query.UserSignUpQuery
import org.springframework.stereotype.Service

@Service
class UserService(

) : UserUseCase {

    override fun signUp(userSignUpQuery: UserSignUpQuery) {
        TODO()
    }
}