package com.assignment.caffe.domain.service

import com.appmattus.kotlinfixture.kotlinFixture
import com.assignment.caffe.application.domain.service.UserService
import com.assignment.caffe.application.port.out.UserPort
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.*
import java.sql.SQLException

class UserServiceTest : BehaviorSpec({

    val userPort = mockk<UserPort>()
    lateinit var userService: UserService

    beforeAny {
        userService = spyk(UserService(userPort))
    }

    Given("signUp 요청이 들어온 경우") {
        When("정상적으로 처리되는 경우") {

            every { userPort.insertUser(any()) } just Runs
            val userSignUpQuery = kotlinFixture()

            Then("함수가 아무것도 반환하지 않고 종료된다.") {
                userService.signUp(userSignUpQuery())
            }
        }
        When("예외가 발생하는 경우") {
            val userSignUpQuery = kotlinFixture()

            every { userPort.insertUser(any()) } throws SQLException()

            Then("상위 레이어에 예외가 전달된다.") {
                shouldThrow<SQLException> {
                    userService.signUp(userSignUpQuery())
                }
            }
        }
    }
})