package com.assignment.caffe.application.port.`in`.query

import com.assignment.caffe.application.domain.model.User

fun UserSignUpQuery.toEntity() = User(
    id = 0,
    phoneNumber = this.phoneNumber,
    password = this.password,
)