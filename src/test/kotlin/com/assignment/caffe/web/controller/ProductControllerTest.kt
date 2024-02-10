package com.assignment.caffe.web.controller

import com.assignment.caffe.adapter.`in`.web.ProductController
import com.assignment.caffe.application.port.`in`.ProductUseCase
import com.assignment.caffe.web.controller.fixture.createProductRequestBuild
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@SpringBootTest
@AutoConfigureWebTestClient
class ProductControllerTest : BehaviorSpec({

    val productUseCase = mockk<ProductUseCase>()
    val productController = ProductController(productUseCase)

    val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(productController).build()
    val objectMapper: ObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    Given("상품 등록 요청 시") {

        // 테스트용 principal 객체 생성
        val principal = TestingAuthenticationToken("username", "password", "ROLE_USER")
        SecurityContextHolder.getContext().authentication = principal

        every { productUseCase.createProduct(any()) } just Runs

        When("정상적인 parameter가 들어온 경우") {

            val request = createProductRequestBuild()

            Then("201 Created") {
                mockMvc.perform(
                    MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(MockMvcResultMatchers.status().isCreated)
            }
        }

        When("상품명이 100자를 초과하는 경우") {

            val request = createProductRequestBuild {
                name = "a".repeat(101)
            }

            Then("400 Bad Request") {
                mockMvc.perform(
                    MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(MockMvcResultMatchers.status().isBadRequest)
            }
        }

        When("상품 설명이 1000자를 초과하는 경우") {

            val request = createProductRequestBuild {
                description = "a".repeat(1001)
            }

            Then("400 Bad Request") {
                mockMvc.perform(
                    MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(MockMvcResultMatchers.status().isBadRequest)
            }
        }

        When("상품 판매 가격이 0인 경우") {

            val request = createProductRequestBuild {
                salePrice = 0
            }

            Then("400 Bad Request") {
                mockMvc.perform(
                    MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(MockMvcResultMatchers.status().isBadRequest)
            }
        }

        When("상품 원가가 음수인 경우") {

            val request = createProductRequestBuild {
                originPrice = -1
            }

            Then("400 Bad Request") {
                mockMvc.perform(
                    MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(MockMvcResultMatchers.status().isBadRequest)
            }
        }
    }
})
