package com.assignment.caffe.application.port.out

import com.assignment.caffe.application.domain.dto.UserTokenDto
import org.springframework.security.crypto.password.PasswordEncoder

interface AuthPort {
    fun getEncryptedObject(): PasswordEncoder

    fun generateAccessTokenAndRefreshToken(str: String): UserTokenDto

    fun generateAccessToken(str: String): String

    fun generateRefreshToken(): String
}
