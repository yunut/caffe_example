package com.assignment.caffe.application.domain.model

data class UserToken(
    val token: String
) {
    companion object {
        fun of(token: String): UserToken {
            return UserToken(token)
        }
    }
}