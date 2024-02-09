package com.assignment.caffe.adapter.`in`.web.security

import com.assignment.caffe.adapter.`in`.web.security.jwt.JwtProvider
import com.assignment.caffe.application.domain.dto.UserTokenDto
import com.assignment.caffe.application.port.out.AuthPort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class SecurityAdapter(
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider,
) : AuthPort {
    override fun getEncryptedObject(): PasswordEncoder {
        return passwordEncoder
    }

    override fun generateAccessTokenAndRefreshToken(str: String): UserTokenDto {
        val accessToken = generateAccessToken(str)
        val refreshToken = generateRefreshToken()
        return UserTokenDto(accessToken, refreshToken)
    }

    override fun generateAccessToken(str: String): String {
        return jwtProvider.generateAccessToken(str)
    }

    override fun generateRefreshToken(): String {
        return jwtProvider.generateRefreshToken()
    }
}
