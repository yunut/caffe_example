package com.assignment.caffe.application.port.out

import com.assignment.caffe.application.domain.model.User

interface UserPort {

    fun insertUser(user: User)

    fun existsUserByPhoneNumber(phoneNumber: String): Boolean
}