package com.assignment.caffe.web.controller

import com.assignment.caffe.adapter.`in`.web.UserController
import com.assignment.caffe.adapter.`in`.web.request.SignUpRequest
import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.port.`in`.UserUseCase
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import jakarta.servlet.ServletException
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerTest : BehaviorSpec({

    val userUseCase = mockk<UserUseCase>()
    val userController = UserController(userUseCase)

    val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(userController).build()
    val objectMapper: ObjectMapper = jacksonObjectMapper()

    Given("회원가입 요청 시") {

        When("정상적인 parameter가 들어온 경우") {

            val request = SignUpRequest(
                phoneNumber = "010-1234-5678",
                password = "password"
            )

            every { userUseCase.signUp(any()) } just Runs

            Then("201 Created") {
                mockMvc.perform(
                    MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andExpect(MockMvcResultMatchers.status().isCreated)
            }
        }

        When("잘못된 형식의 전화번호 parameter가 들어온 경우") {

            val request1 = SignUpRequest(
                phoneNumber = "0101234-5678",
                password = "password"
            )
            val request2 = SignUpRequest(
                phoneNumber = "010-1234-56789",
                password = "password"
            )

            every { userUseCase.signUp(any()) } just Runs

            Then("400 Bad Request") {
                mockMvc.perform(
                    MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1))
                ).andExpect(MockMvcResultMatchers.status().isBadRequest)

                mockMvc.perform(
                    MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2))
                ).andExpect(MockMvcResultMatchers.status().isBadRequest)

            }
        }

        When("이미 존재하는 전화번호 parameter가 들어온 경우") {


            every { userUseCase.signUp(any()) } throws ConflictException("이미 존재하는 전화번호입니다.")

            Then("409 Conflict") {
                val exception = shouldThrow<ServletException> {
                    mockMvc.perform(
                        MockMvcRequestBuilders.post("/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(SignUpRequest(
                                phoneNumber = "010-1234-5678",
                                password = "password"
                            ))
                            )
                    )
                }
                exception.message!!.contains("ConflictException") shouldBe true
            }
        }
    }
})
