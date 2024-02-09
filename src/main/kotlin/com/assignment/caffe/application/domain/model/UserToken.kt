package com.assignment.caffe.application.domain.model

data class UserToken(
    val accessToken: String,
    val refreshToken: String,
) {
    companion object {
        fun of(accessToken: String, refreshToken: String): UserToken {
            return UserToken(accessToken, refreshToken)
        }
    }
}
