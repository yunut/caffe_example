package com.assignment.caffe.web.controller

import com.assignment.caffe.adapter.`in`.web.ProductController
import com.assignment.caffe.adapter.`in`.web.response.GetProductListResponse
import com.assignment.caffe.adapter.`in`.web.response.GetProductResponse
import com.assignment.caffe.adapter.`in`.web.response.MetaBody
import com.assignment.caffe.adapter.`in`.web.response.ResponseBody
import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.domain.exception.NotFoundException
import com.assignment.caffe.application.port.`in`.ProductUseCase
import com.assignment.caffe.persistence.repository.fixture.baseProductBuild
import com.assignment.caffe.web.controller.fixture.createProductRequestBuild
import com.assignment.caffe.web.controller.fixture.updateProductRequestBuild
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
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
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.sql.SQLException

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

        When("이미 존재하는 이름의 상품을 등록하는 경우") {

            val request = createProductRequestBuild()

            every { productUseCase.createProduct(any()) } throws ConflictException("이미 존재하는 상품입니다.")

            Then("409 Conflict") {
                val exception = shouldThrow<ServletException> {
                    mockMvc.perform(
                        MockMvcRequestBuilders.post("/product")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                objectMapper.writeValueAsString(
                                    request,
                                ),
                            ),
                    )
                }
                exception.message!!.contains("ConflictException") shouldBe true
            }
        }
    }

    Given("상품 부분 수정 요청 시") {

        every { productUseCase.updateProduct(any()) } just Runs

        When("정상적인 parameter가 들어온 경우") {

            val request = updateProductRequestBuild()

            Then("200 OK") {
                mockMvc.perform(
                    MockMvcRequestBuilders.patch("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(MockMvcResultMatchers.status().isOk)
            }
        }

        When("상품명이 100자를 초과하는 경우") {

            val request = updateProductRequestBuild {
                name = "a".repeat(101)
            }

            Then("400 Bad Request") {
                mockMvc.perform(
                    MockMvcRequestBuilders.patch("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(MockMvcResultMatchers.status().isBadRequest)
            }
        }

        When("상품 설명이 1000자를 초과하는 경우") {

            val request = updateProductRequestBuild {
                description = "a".repeat(1001)
            }

            Then("400 Bad Request") {
                mockMvc.perform(
                    MockMvcRequestBuilders.patch("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(MockMvcResultMatchers.status().isBadRequest)
            }
        }

        When("상품 판매 가격이 0인 경우") {

            val request = updateProductRequestBuild {
                salePrice = 0
            }

            Then("400 Bad Request") {
                mockMvc.perform(
                    MockMvcRequestBuilders.patch("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(MockMvcResultMatchers.status().isBadRequest)
            }
        }

        When("상품 원가가 음수인 경우") {

            val request = updateProductRequestBuild {
                originPrice = -1
            }

            Then("400 Bad Request") {
                mockMvc.perform(
                    MockMvcRequestBuilders.patch("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                ).andExpect(MockMvcResultMatchers.status().isBadRequest)
            }
        }
    }

    Given("상품 삭제 요청 시") {

        every { productUseCase.deleteProduct(any()) } just Runs

        When("정상적으로 상품이 삭제 된 경우") {

            Then("200 ok") {
                mockMvc.perform(
                    MockMvcRequestBuilders.delete("/product/1"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
            }
        }
        When("정상적으로 상품이 삭제 되지 않은 경우") {

            every { productUseCase.deleteProduct(any()) } throws Exception("상품 삭제 중 오류가 발생했습니다.")

            Then("500 Internal Server Error") {
                val exception = shouldThrow<ServletException> {
                    mockMvc.perform(
                        MockMvcRequestBuilders.delete("/product/1"),
                    )
                }
                exception.message!!.contains("Exception") shouldBe true
            }
        }
    }

    Given("상품 조회 요청 시") {

        val product = baseProductBuild()
        every { productUseCase.getProduct(any(), any()) } returns product

        When("정상적으로 상품이 조회 된 경우") {

            Then("200 ok와 데이터가 반환된다.") {
                mockMvc.perform(
                    MockMvcRequestBuilders.get("/product/1"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                    .andReturn().response.contentAsString shouldBe objectMapper.writeValueAsString(
                    ResponseBody(
                        MetaBody(
                            HttpStatus.OK.value(),
                            "Product retrieved successfully",
                        ),
                        GetProductResponse.toResponse(product),
                    ),
                )
            }
        }
        When("상품이 조회 되지 않은 경우") {

            every { productUseCase.getProduct(any(), any()) } throws NotFoundException("상품이 존재하지 않습니다.")

            Then("400에러와 Not Found 메시지가 전달된다.") {
                val exception = shouldThrow<ServletException> {
                    mockMvc.perform(
                        MockMvcRequestBuilders.get("/product/1"),
                    )
                }
                exception.message!!.contains("NotFoundException") shouldBe true
            }
        }
    }

    Given("상품 리스트 조회 요청 시") {

        val products = listOf(baseProductBuild(), baseProductBuild())

        every { productUseCase.getProductsWithCursor(any(), any(), any(), any()) } returns products

        When("정상적으로 상품 리스트가 조회 된 경우 데이터가 존재하면") {

            Then("200 ok와 데이터가 반환된다.") {
                mockMvc.perform(
                    MockMvcRequestBuilders.get("/product/list"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                    .andReturn().response.contentAsString shouldBe objectMapper.writeValueAsString(
                    ResponseBody(
                        MetaBody(
                            HttpStatus.OK.value(),
                            "Product list retrieved successfully",
                        ),
                        GetProductListResponse.toResponse(products),
                    ),
                )
            }
        }

        When("정상적으로 상품 리스트가 조회 된 경우 데이터가 존재하지 않으면") {

            every { productUseCase.getProductsWithCursor(any(), any(), any(), any()) } returns emptyList()

            Then("200 ok와 빈 배열이 반환된다.") {
                mockMvc.perform(
                    MockMvcRequestBuilders.get("/product/list"),
                ).andExpect(MockMvcResultMatchers.status().isOk)
                    .andReturn().response.contentAsString shouldBe objectMapper.writeValueAsString(
                    ResponseBody(
                        MetaBody(
                            HttpStatus.OK.value(),
                            "Product list retrieved successfully",
                        ),
                        GetProductListResponse.toResponse(emptyList()),
                    ),
                )
            }
        }

        When("상품 리스트가 정상적으로 조회 되지 않은 경우") {

            every { productUseCase.getProductsWithCursor(any(), any(), any(), any()) } throws SQLException("상품 리스트 조회 중 오류가 발생했습니다.")

            Then("500 에러와 Exception 메시지가 전달된다.") {
                val exception = shouldThrow<ServletException> {
                    mockMvc.perform(
                        MockMvcRequestBuilders.get("/product/list"),
                    )
                }
                exception.message!!.contains("SQLException") shouldBe true
            }
        }
    }
})
