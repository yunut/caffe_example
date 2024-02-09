package com.assignment.caffe.web.controller

import com.assignment.caffe.adapter.`in`.web.SignController
import com.assignment.caffe.adapter.`in`.web.request.SignUpRequest
import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.domain.model.UserToken
import com.assignment.caffe.application.port.`in`.SignUseCase
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
class SignControllerTest : BehaviorSpec({

    val signUseCase = mockk<SignUseCase>()
    val signController = SignController(signUseCase)

    val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(signController).build()
    val objectMapper: ObjectMapper = jacksonObjectMapper()

    Given("회원가입 요청 시") {

        When("정상적인 parameter가 들어온 경우") {

            val request = SignUpRequest(
                phoneNumber = "010-1234-5678",
                password = "password",
            )

            every { signUseCase.signUp(any()) } just Runs

            Then("201 Created") {
                mockMvc.perform(
                    MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(MockMvcResultMatchers.status().isCreated)
            }
        }

        When("잘못된 형식의 전화번호 parameter가 들어온 경우") {

            val request1 = SignUpRequest(
                phoneNumber = "0101234-5678",
                password = "password",
            )
            val request2 = SignUpRequest(
                phoneNumber = "010-1234-56789",
                password = "password",
            )

            every { signUseCase.signUp(any()) } just Runs

            Then("400 Bad Request") {
                mockMvc.perform(
                    MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)),
                ).andExpect(MockMvcResultMatchers.status().isBadRequest)

                mockMvc.perform(
                    MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)),
                ).andExpect(MockMvcResultMatchers.status().isBadRequest)
            }
        }

        When("이미 존재하는 전화번호 parameter가 들어온 경우") {

            every { signUseCase.signUp(any()) } throws ConflictException("이미 존재하는 전화번호입니다.")

            Then("409 Conflict") {
                val exception = shouldThrow<ServletException> {
                    mockMvc.perform(
                        MockMvcRequestBuilders.post("/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                objectMapper.writeValueAsString(
                                    SignUpRequest(
                                        phoneNumber = "010-1234-5678",
                                        password = "password",
                                    ),
                                ),
                            ),
                    )
                }
                exception.message!!.contains("ConflictException") shouldBe true
            }
        }
    }

    Given("로그인 요청 시") {

        val request = SignUpRequest(
            phoneNumber = "010-1234-5678",
            password = "password",
        )

        When("로그인이 성공할 경우") {

            every { signUseCase.signIn(any()) } returns UserToken("accessToken", "refreshToken")

            Then("200 OK와 토큰이 발급된다.") {
                mockMvc.perform(
                    MockMvcRequestBuilders.post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(MockMvcResultMatchers.status().isOk)
            }
        }
        When("로그인이 실패할 경우") {

            every { signUseCase.signIn(any()) } throws Exception("토큰 검증 실패")
            Then("401 Unauthorized 가 발생된다.") {

                // TODO Service Exception 작성 필요
                val exception =
                    shouldThrow<ServletException> {
                        mockMvc.perform(
                            MockMvcRequestBuilders.post("/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)),
                        )
                    }
                exception.message!!.contains("토큰 검증 실패") shouldBe true
            }
        }
    }
})
