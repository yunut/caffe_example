package com.assignment.caffe.application.port.`in`.query

import org.jetbrains.annotations.NotNull


data class UserSignUpQuery(
    @field:NotNull
    val phoneNumber: String,
    @field:NotNull
    val password: String,
) {
    companion object {
        fun of(phoneNumber: String, password: String): UserSignUpQuery {
            return UserSignUpQuery(phoneNumber, password)
        }
    }
}

data class UserSignInQuery(
    @field:NotNull
    val phoneNumber: String,
    @field:NotNull
    val password: String,
) {
    companion object {
        fun of(phoneNumber: String, password: String): UserSignInQuery {
            return UserSignInQuery(phoneNumber, password)
        }
    }
}