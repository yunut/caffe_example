package com.assignment.caffe.application.domain.service

import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.port.`in`.UserUseCase
import com.assignment.caffe.application.port.`in`.query.UserSignUpQuery
import com.assignment.caffe.application.port.`in`.query.toEntity
import com.assignment.caffe.application.port.out.UserPort
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userPort: UserPort,
) : UserUseCase {

    // TODO Spring Security encryption
    override fun signUp(userSignUpQuery: UserSignUpQuery) {
        val user = userSignUpQuery.toEntity()

        if(userPort.existsUserByPhoneNumber(user.phoneNumber))
            throw ConflictException("User already exists")

        userPort.insertUser(user)
    }
}