package com.assignment.caffe.application.port.`in`.query

import org.jetbrains.annotations.NotNull

data class SignUpQuery(
    @field:NotNull
    val phoneNumber: String,
    @field:NotNull
    val password: String,
) {
    companion object {
        fun of(phoneNumber: String, password: String): SignUpQuery {
            return SignUpQuery(phoneNumber, password)
        }
    }
}

data class SignInQuery(
    @field:NotNull
    val phoneNumber: String,
    @field:NotNull
    val password: String,
) {
    companion object {
        fun of(phoneNumber: String, password: String): SignInQuery {
            return SignInQuery(phoneNumber, password)
        }
    }
}
