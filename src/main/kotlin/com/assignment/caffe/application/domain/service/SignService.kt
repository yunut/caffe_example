package com.assignment.caffe.application.domain.service

import com.assignment.caffe.application.domain.dto.UserTokenDto
import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.domain.exception.NotMatchException
import com.assignment.caffe.application.domain.model.UserToken
import com.assignment.caffe.application.port.`in`.SignUseCase
import com.assignment.caffe.application.port.`in`.query.SignInQuery
import com.assignment.caffe.application.port.`in`.query.SignUpQuery
import com.assignment.caffe.application.port.`in`.query.toEntity
import com.assignment.caffe.application.port.out.AuthPort
import com.assignment.caffe.application.port.out.UserPort
import com.assignment.caffe.application.port.out.UserTokenPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SignService(
    private val userPort: UserPort,
    private val authPort: AuthPort,
    private val userRefreshTokenPort: UserTokenPort,
) : SignUseCase {

    @Transactional
    override fun signUp(signUpQuery: SignUpQuery) {
        val user = signUpQuery.toEntity(authPort.getEncryptedObject())

        if (userPort.existsUserByPhoneNumber(user.phoneNumber)) {
            throw ConflictException("User already exists")
        }

        userPort.insertUser(user)
    }

    @Transactional
    override fun signIn(signInQuery: SignInQuery): UserTokenDto {
        val user = userPort.findUserByPhoneNumber(signInQuery.phoneNumber)
            ?.takeIf {
                authPort.getEncryptedObject().matches(signInQuery.password, it.password)
            } ?: throw NotMatchException("아이디 또는 비밀번호가 일치하지 않습니다.")

        val userToken = authPort.generateAccessTokenAndRefreshToken("${user.id!!}:${user.phoneNumber}")	// 토큰 생성

        userRefreshTokenPort.findByUserId(user.id)?.updateRefreshToken(userToken.refreshToken)
            ?: userRefreshTokenPort.insertToken(UserToken(user, userToken.accessToken, userToken.refreshToken))
        return UserTokenDto.of(userToken.accessToken, userToken.refreshToken)
    }

    @Transactional
    override fun signOut(userId: Int) {
        userRefreshTokenPort.deleteTokenByUserId(userId)
    }
}
