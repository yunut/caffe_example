package com.assignment.caffe.application.port.out

import com.assignment.caffe.application.domain.model.UserToken
import org.springframework.security.crypto.password.PasswordEncoder

interface AuthPort {
    fun getEncryptedObject(): PasswordEncoder

    fun generateAccessTokenAndRefreshToken(str: String): UserToken

    fun generateAccessToken(str: String): String

    fun generateRefreshToken(): String
}
