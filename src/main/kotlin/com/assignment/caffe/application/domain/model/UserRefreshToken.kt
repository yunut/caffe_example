package com.assignment.caffe.application.domain.model

class UserRefreshToken private constructor(
    val key: String,
    val refreshToken: String,
) {

    companion object {

        val prefix = "refreshToken:"

        fun of(key: String, refreshToken: String): UserRefreshToken {
            return UserRefreshToken("$prefix$key", refreshToken)
        }
    }
}
