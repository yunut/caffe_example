package com.assignment.caffe.application.domain.dto

data class UserTokenDto(
    val accessToken: String,
    val refreshToken: String,
) {
    companion object {
        fun of(accessToken: String, refreshToken: String): UserTokenDto {
            return UserTokenDto(accessToken, refreshToken)
        }
    }
}
