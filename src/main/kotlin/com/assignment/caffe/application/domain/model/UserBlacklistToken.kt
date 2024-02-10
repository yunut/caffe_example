package com.assignment.caffe.application.domain.model
class UserBlacklistToken private constructor(
    val key: String,
    val accessToken: String,
) {
    companion object {

        val prefix = "blacklistToken:"
        fun of(key: String, accessToken: String): UserBlacklistToken {
            return UserBlacklistToken("$prefix$key", accessToken)
        }
    }
}
