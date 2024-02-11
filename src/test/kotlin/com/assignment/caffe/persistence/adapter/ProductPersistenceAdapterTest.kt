package com.assignment.caffe.persistence.adapter

import com.assignment.caffe.adapter.out.persistence.ProductPersistenceAdapter
import com.assignment.caffe.adapter.out.persistence.config.QueryDslConfig
import com.assignment.caffe.adapter.out.persistence.repository.ConsonantRepository
import com.assignment.caffe.adapter.out.persistence.repository.ProductRepository
import com.assignment.caffe.persistence.repository.fixture.baseProductBuild
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig::class)
class ProductPersistenceAdapterTest(
    @Autowired private val productRepository: ProductRepository,
    @Autowired private val consonantRepository: ConsonantRepository,
) : BehaviorSpec() {

    val productPersistenceAdapter = ProductPersistenceAdapter(productRepository, consonantRepository)

    init {
        Given("상품 자음 테이블 저장 요청 어댑터 메소드 호출 시") {

            productPersistenceAdapter.saveConsonant(baseProductBuild { name = "테스트" })

            When("정상적으로 저장될 경우") {
                Then("상품 자음으로 저장된다.") {
                    consonantRepository.findAll().first().frontConsonant shouldBe "ㅌㅅㅌ"
                }
            }
        }
    }
}
