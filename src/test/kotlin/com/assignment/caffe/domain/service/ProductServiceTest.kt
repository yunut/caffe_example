package com.assignment.caffe.domain.service

import com.appmattus.kotlinfixture.kotlinFixture
import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.domain.service.ProductService
import com.assignment.caffe.application.port.out.ProductPort
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.*
import java.sql.SQLException

class ProductServiceTest : BehaviorSpec({

    val productPort = mockk<ProductPort>()
    lateinit var productService: ProductService

    beforeAny {
        productService = spyk(
            ProductService(productPort),
        )
    }

    Given("상품 등록 요청이 들어온 경우") {
        val createProductQuery = kotlinFixture()

        every { productPort.insertProduct(any()) } just Runs
        every { productPort.existsProductByName(any()) } returns false

        When("정상적으로 처리되는 경우") {

            Then("함수가 아무것도 반환하지 않고 종료된다.") {
                productService.createProduct(createProductQuery())
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
})
