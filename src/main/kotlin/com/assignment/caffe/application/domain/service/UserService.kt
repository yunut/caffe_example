package com.assignment.caffe.application.domain.service

import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.domain.exception.NotMatchException
import com.assignment.caffe.application.domain.model.UserRefreshToken
import com.assignment.caffe.application.domain.model.UserToken
import com.assignment.caffe.application.port.`in`.UserUseCase
import com.assignment.caffe.application.port.`in`.query.UserSignInQuery
import com.assignment.caffe.application.port.`in`.query.UserSignUpQuery
import com.assignment.caffe.application.port.`in`.query.toEntity
import com.assignment.caffe.application.port.out.AuthPort
import com.assignment.caffe.application.port.out.UserPort
import com.assignment.caffe.application.port.out.UserRefreshTokenPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userPort: UserPort,
    private val authPort: AuthPort,
    private val userRefreshTokenPort: UserRefreshTokenPort,
) : UserUseCase {

    @Transactional
    override fun signUp(userSignUpQuery: UserSignUpQuery) {
        val user = userSignUpQuery.toEntity(authPort.getEncryptedObject())

        if (userPort.existsUserByPhoneNumber(user.phoneNumber)) {
            throw ConflictException("User already exists")
        }

        userPort.insertUser(user)
    }

    override fun signIn(userSignInQuery: UserSignInQuery): UserToken {
        val user = userPort.findUserByPhoneNumber(userSignInQuery.phoneNumber)
            ?.takeIf {
                authPort.getEncryptedObject().matches(userSignInQuery.password, it.password)
            } ?: throw NotMatchException("아이디 또는 비밀번호가 일치하지 않습니다.")

        val userToken = authPort.generateAccessTokenAndRefreshToken("{${user.id!!}:${user.phoneNumber}}")	// 토큰 생성

        userRefreshTokenPort.findByUserId(user.id)?.updateRefreshToken(userToken.refreshToken)
            ?: userRefreshTokenPort.insertRefreshToken(UserRefreshToken(user, userToken.refreshToken))
        return UserToken.of(userToken.accessToken, userToken.refreshToken)
    }
}
