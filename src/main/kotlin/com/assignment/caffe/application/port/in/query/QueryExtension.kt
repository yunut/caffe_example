package com.assignment.caffe.application.port.`in`.query

import com.assignment.caffe.application.domain.model.User
import org.springframework.security.crypto.password.PasswordEncoder

fun UserSignUpQuery.toEntity(passwordEncoder: PasswordEncoder) = User(
    id = 0,
    phoneNumber = this.phoneNumber,
    password = passwordEncoder.encode(this.password),
)