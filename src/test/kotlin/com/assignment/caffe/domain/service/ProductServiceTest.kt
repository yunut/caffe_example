package com.assignment.caffe.domain.service

import com.appmattus.kotlinfixture.kotlinFixture
import com.assignment.caffe.application.domain.enumeration.ProductSort
import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.domain.exception.NotFoundException
import com.assignment.caffe.application.domain.service.ProductService
import com.assignment.caffe.application.port.out.ProductPort
import com.assignment.caffe.persistence.repository.fixture.baseProductBuild
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.SQLException
import java.util.UUID

class ProductServiceTest : BehaviorSpec({

    val productPort = mockk<ProductPort>()
    lateinit var productService: ProductService

    val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    beforeAny {
        productService = spyk(
            withContext(Dispatchers.IO) {
                ProductService(productPort)
            },
        )
    }

    Given("상품 등록 요청이 들어온 경우") {
        val createProductQuery = kotlinFixture()

        every { productPort.insertProduct(any()) } just Runs
        every { productPort.saveConsonant(any()) } just Runs
        every { productPort.existsProductByName(any()) } returns false

        When("정상적으로 처리되는 경우") {

            Then("상품 id를 반환한다. (jpa에서 자동 생성되는 id를 반환)") {
                withContext(Dispatchers.IO) {
                    productService.createProduct(createProductQuery()) shouldBe "null"
                }
            }
        }

        When("상품 등록 시 이미 존재하는 상품이 있는 경우") {

            every { productPort.existsProductByName(any()) } returns true

            Then("ConflictException이 발생한다.") {
                shouldThrow<ConflictException> {
                    productService.createProduct(createProductQuery())
                }
            }
        }

        When("예외가 발생하는 경우") {

            every { productPort.insertProduct(any()) } throws SQLException()
            every { productPort.existsProductByName(any()) } returns false

            Then("요청 어댑터에 예외가 전달된다.") {
                shouldThrow<SQLException> {
                    productService.createProduct(createProductQuery())
                }
            }
        }
    }

    Given("상품 부분 수정 시") {
        val updateProductQuery = kotlinFixture()

        every { productPort.updateProduct(any()) } just Runs
        every { productPort.saveConsonant(any()) } just Runs
        every { productPort.existsProductById(any()) } returns true
        every { productPort.findProductById(any()) } returns baseProductBuild()

        When("정상적으로 처리되는 경우") {

            every { productPort.findProductById(any()) } returns baseProductBuild()

            Then("함수가 아무것도 반환하지 않고 종료된다.") {
                withContext(Dispatchers.IO) {
                    productService.updateProduct(updateProductQuery())
                }
            }
        }

        When("상품 수정 시 존재하지 않는 상품이 있는 경우") {

            every { productPort.existsProductById(any()) } returns false

            Then("NotFoundException 발생한다.") {
                shouldThrow<NotFoundException> {
                    productService.updateProduct(updateProductQuery())
                }
            }
        }

        When("예외가 발생하는 경우") {

            every { productPort.updateProduct(any()) } throws SQLException()
            every { productPort.existsProductById(any()) } returns true

            Then("요청 어댑터에 예외가 전달된다.") {
                shouldThrow<SQLException> {
                    productService.updateProduct(updateProductQuery())
                }
            }
        }
    }

    Given("상품 삭제 요청이 들어온 경우") {
        val id = "1"

        every { productPort.deleteProduct(any()) } just Runs

        When("정상적으로 처리되는 경우") {

            Then("함수가 아무것도 반환하지 않고 종료된다.") {
                withContext(Dispatchers.IO) {
                    productService.deleteProduct(id)
                }
            }
        }

        When("예외가 발생하는 경우") {

            every { productPort.deleteProduct(any()) } throws SQLException()

            Then("요청 어댑터에 예외가 전달된다.") {
                shouldThrow<SQLException> {
                    productService.deleteProduct(id)
                }
            }
        }
    }

    Given("상품 조회 요청이 들어온 경우") {
        val productId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()
        val product = baseProductBuild()
        every { productPort.getProduct(any(), any()) } returns product

        When("정상적으로 처리되는 경우") {

            Then("상품 데이터가 반환된다.") {
                withContext(Dispatchers.IO) {
                    objectMapper.writeValueAsString(productService.getProduct(productId, userId)) shouldBe objectMapper.writeValueAsString(product)
                }
            }
        }

        When("상품 조회 시 존재하지 않는 상품이 있는 경우") {

            every { productPort.getProduct(any(), any()) } returns null

            Then("NotFoundException 발생한다.") {
                shouldThrow<NotFoundException> {
                    productService.getProduct(productId, userId)
                }
            }
        }

        When("예외가 발생하는 경우") {

            every { productPort.getProduct(any(), any()) } throws SQLException()

            Then("요청 어댑터에 예외가 전달된다.") {
                shouldThrow<SQLException> {
                    productService.getProduct(productId, userId)
                }
            }
        }
    }

    Given("상품 목록 조회 요청이 들어온 경우") {
        val userId = UUID.randomUUID().toString()
        val size = 10
        val sort = ProductSort.NAME_ASC
        val products =
            // 상품 목록 100개 이름 바꿔서
            (1..100).map { baseProductBuild { name = "상품$it" } }
        every { productPort.getProductsWithCursor(any(), any(), any(), any()) } returns products

        When("정상적으로 처리되는 경우") {

            Then("상품 목록 데이터가 반환된다.") {
                withContext(Dispatchers.IO) {
                    objectMapper.writeValueAsString(productService.getProductsWithCursor(userId, size, sort, null)) shouldBe objectMapper.writeValueAsString(products)
                }
            }
        }

        When("예외가 발생하는 경우") {

            every { productPort.getProductsWithCursor(any(), any(), any(), any()) } throws SQLException()

            Then("요청 어댑터에 예외가 전달된다.") {
                shouldThrow<SQLException> {
                    productService.getProductsWithCursor(userId, size, sort, null)
                }
            }
        }
    }

    Given("상품 검색 요청이 들어온 경우") {

        val products = listOf(baseProductBuild())
        When("정상적으로 처리되는 경우") {

            every { productPort.searchProduct(any()) } returns products

            Then("상품 목록 데이터가 반환된다.") {
                withContext(Dispatchers.IO) {
                    objectMapper.writeValueAsString(productService.searchProduct("테스트")) shouldBe objectMapper.writeValueAsString(products)
                }
            }
        }
        When("예외가 발생하는 경우") {

            every { productPort.searchProduct(any()) } throws SQLException()
            Then("요청 어댑터에 예외가 전달된다.") {
                shouldThrow<SQLException> {
                    productService.searchProduct("테스트")
                }
            }
        }
    }
})
