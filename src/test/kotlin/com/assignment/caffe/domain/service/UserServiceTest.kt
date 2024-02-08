package com.assignment.caffe.domain.service

import com.appmattus.kotlinfixture.kotlinFixture
import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.domain.service.UserService
import com.assignment.caffe.application.port.out.AuthPort
import com.assignment.caffe.application.port.out.UserPort
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.*
import java.sql.SQLException

class UserServiceTest : BehaviorSpec({

    val userPort = mockk<UserPort>()
    val authPort = mockk<AuthPort>()
    lateinit var userService: UserService

    beforeAny {
        userService = spyk(UserService(userPort, authPort))
    }

    Given("signUp 요청이 들어온 경우") {

        every { authPort.getEncryptedObject().encode(any()) } returns "1"

        When("정상적으로 처리되는 경우") {

            every { userPort.insertUser(any()) } just Runs
            every { userPort.existsUserByPhoneNumber(any()) } returns false
            val userSignUpQuery = kotlinFixture()

            Then("함수가 아무것도 반환하지 않고 종료된다.") {
                userService.signUp(userSignUpQuery())
            }
        }

        When("이미 존재하는 유저가 있는 경우") {
            val userSignUpQuery = kotlinFixture()

            every { userPort.existsUserByPhoneNumber(any()) } returns true

            Then("ConflictException이 발생한다.") {
                shouldThrow<ConflictException> {
                    userService.signUp(userSignUpQuery())
                }
            }
        }

        When("예외가 발생하는 경우") {
            val userSignUpQuery = kotlinFixture()

            every { userPort.insertUser(any()) } throws SQLException()
            every { userPort.existsUserByPhoneNumber(any()) } returns false

            Then("상위 레이어에 예외가 전달된다.") {
                shouldThrow<SQLException> {
                    userService.signUp(userSignUpQuery())
                }
            }
        }
    }
})