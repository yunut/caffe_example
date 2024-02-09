package com.assignment.caffe.domain.service

import com.appmattus.kotlinfixture.kotlinFixture
import com.assignment.caffe.application.domain.dto.UserTokenDto
import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.domain.exception.NotMatchException
import com.assignment.caffe.application.domain.model.User
import com.assignment.caffe.application.domain.model.UserToken
import com.assignment.caffe.application.domain.service.SignService
import com.assignment.caffe.application.port.out.AuthPort
import com.assignment.caffe.application.port.out.UserPort
import com.assignment.caffe.application.port.out.UserTokenPort
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.SQLException

class SignServiceTest : BehaviorSpec({

    val userPort = mockk<UserPort>()
    val authPort = mockk<AuthPort>()
    val userRefreshTokenPort = mockk<UserTokenPort>()
    lateinit var signService: SignService

    beforeAny {
        signService = spyk(
            withContext(Dispatchers.IO) {
                SignService(userPort, authPort, userRefreshTokenPort)
            },
        )
    }

    Given("signUp 요청이 들어온 경우") {

        every { authPort.getEncryptedObject().encode(any()) } returns "1"

        When("정상적으로 처리되는 경우") {

            every { userPort.insertUser(any()) } just Runs
            every { userPort.existsUserByPhoneNumber(any()) } returns false
            val signUpQuery = kotlinFixture()

            Then("함수가 아무것도 반환하지 않고 종료된다.") {
                withContext(Dispatchers.IO) {
                    signService.signUp(signUpQuery())
                }
            }
        }

        When("이미 존재하는 유저가 있는 경우") {
            val signUpQuery = kotlinFixture()

            every { userPort.existsUserByPhoneNumber(any()) } returns true

            Then("ConflictException이 발생한다.") {
                shouldThrow<ConflictException> {
                    signService.signUp(signUpQuery())
                }
            }
        }

        When("예외가 발생하는 경우") {
            val signUpQuery = kotlinFixture()

            every { userPort.insertUser(any()) } throws SQLException()
            every { userPort.existsUserByPhoneNumber(any()) } returns false

            Then("상위 레이어에 예외가 전달된다.") {
                shouldThrow<SQLException> {
                    signService.signUp(signUpQuery())
                }
            }
        }
    }

    Given("Signin 요청이 들어온 경우") {

        every { authPort.generateAccessTokenAndRefreshToken(any()) } returns UserTokenDto("accessToken", "refreshToken")

        When("정상적으로 처리되는 경우") {
            val signInQuery = kotlinFixture()
            val user = User(1, "010-1234-1234", "12341234")

            every { userPort.findUserByPhoneNumber(any()) } returns user
            every { authPort.getEncryptedObject().matches(any(), any()) } returns true
            every { userRefreshTokenPort.findByUserId(any()) } returns UserToken(User(1, "010-1234-1234", "12341234"), "accessToken", "refreshToken")
            every { userRefreshTokenPort.insertToken(any()) } just Runs

            Then("UserToken이 반환된다.") {
                withContext(Dispatchers.IO) {
                    signService.signIn(signInQuery())
                } shouldBe UserTokenDto("accessToken", "refreshToken")
            }
        }

        When("유저가 존재하지 않는 경우") {
            val signInQuery = kotlinFixture()

            every { userPort.findUserByPhoneNumber(any()) } returns null

            Then("NotMatchException이 발생한다.") {
                shouldThrow<NotMatchException> {
                    signService.signIn(signInQuery())
                }
            }
        }

        When("비밀번호가 일치하지 않는 경우") {
            val signInQuery = kotlinFixture()

            every { userPort.findUserByPhoneNumber(any()) } returns signInQuery<User>()
            every { authPort.getEncryptedObject().matches(any(), any()) } returns false

            Then("NotMatchException이 발생한다.") {
                shouldThrow<NotMatchException> {
                    signService.signIn(signInQuery())
                }
            }
        }
    }

    Given("SignOut 요청이 들어온 경우") {

        When("정상적으로 처리되는 경우") {
            val signOutQuery = kotlinFixture()

            every { userRefreshTokenPort.deleteTokenByUserId(any()) } just Runs

            Then("함수가 아무것도 반환하지 않고 종료된다.") {
                withContext(Dispatchers.IO) {
                    signService.signOut(signOutQuery())
                }
            }
        }

        When("예외가 발생하는 경우") {
            val signOutQuery = kotlinFixture()

            every { userRefreshTokenPort.deleteTokenByUserId(any()) } throws SQLException()

            Then("상위 레이어에 예외가 전달된다.") {
                shouldThrow<SQLException> {
                    signService.signOut(signOutQuery())
                }
            }
        }
    }
})
