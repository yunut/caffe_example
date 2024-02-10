package com.assignment.caffe.persistence.repository

import com.assignment.caffe.adapter.out.persistence.repository.ProductRepository
import com.assignment.caffe.persistence.repository.fixture.createProductBuild
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest(
    @Autowired private val productRepository: ProductRepository,
) : BehaviorSpec() {

    init {
        Given("상품 저장 요청 시") {
            When("name이 중복되지 않는 경우") {
                val product1 = createProductBuild()
                val product2 = createProductBuild {
                    name = "test2"
                }
                Then("정상적으로 종료 된다.") {
                    productRepository.save(product1)
                    productRepository.save(product2)
                }
            }
            When("name이 중복되는 경우") {
                val product1 = createProductBuild()
                val product2 = createProductBuild()
                Then("DataIntegrityViolationException이 발생한다.") {
                    shouldThrow<DataIntegrityViolationException> {
                        productRepository.save(product1)
                        productRepository.save(product2)
                    }
                }
            }
        }
    }
}
