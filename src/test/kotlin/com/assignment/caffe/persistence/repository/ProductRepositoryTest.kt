package com.assignment.caffe.persistence.repository

import com.assignment.caffe.adapter.out.persistence.ProductPersistenceAdapter
import com.assignment.caffe.adapter.out.persistence.config.QueryDslConfig
import com.assignment.caffe.adapter.out.persistence.repository.ConsonantRepository
import com.assignment.caffe.adapter.out.persistence.repository.ProductRepository
import com.assignment.caffe.adapter.out.persistence.repository.impl.ProductRepositoryCustomImpl
import com.assignment.caffe.application.domain.enumeration.ProductSort
import com.assignment.caffe.persistence.repository.fixture.baseConsonantBuild
import com.assignment.caffe.persistence.repository.fixture.baseProductBuild
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import java.time.LocalDateTime

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig::class)
class ProductRepositoryTest(
    @Autowired private val productRepository: ProductRepository,
    @Autowired private val productRepositoryCustom: ProductRepositoryCustomImpl,
    @Autowired private val consonantRepository: ConsonantRepository,
) : BehaviorSpec() {

    init {
        Given("상품 저장 요청 시") {
            When("name이 중복되지 않는 경우") {
                val product1 = baseProductBuild()
                val product2 = baseProductBuild {
                    name = "test2"
                }
                Then("정상적으로 종료 된다.") {
                    withContext(Dispatchers.IO) {
                        productRepository.save(product1)
                        productRepository.save(product2)
                    }
                }
            }
            When("name이 중복되는 경우") {
                val product1 = baseProductBuild()
                val product2 = baseProductBuild()
                Then("DataIntegrityViolationException이 발생한다.") {
                    shouldThrow<DataIntegrityViolationException> {
                        productRepository.save(product1)
                        productRepository.save(product2)
                    }
                }
            }
        }

        Given("상품 목록 조회 요청 시") {

            val size = 10
            val createdBy = "sortTest"

            // 상품을 sortTesta 부터 sortTestz 까지 생성
            val products = ('a'..'z').map {
                baseProductBuild {
                    name = "sortTest$it"
                    this.createdBy = createdBy
                }
            }

            products.forEach {
                productRepository.save(it)
            }
            When("대상이 등록한 상품이 존재하는 경우") {
                Then("상품 목록이 size 만큼 조회된다.") {
                    productRepositoryCustom.findAllProductsWithCursor(createdBy, size, ProductSort.NAME_DESC, null).size shouldBe size
                }
            }

            When("상품이 존재하지 않는 경우") {
                Then("빈 목록이 조회된다.") {
                    productRepositoryCustom.findAllProductsWithCursor("notUser", size, ProductSort.NAME_ASC, null).size shouldBe 0
                }
            }

            When("커서로 다음 페이지를 조회하는 경우") {
                val cursor = productRepositoryCustom.findAllProductsWithCursor(createdBy, size, ProductSort.NAME_DESC, null).last().name
                Then("다음 페이지의 상품 목록이 size 만큼 조회된다.") {
                    val data = productRepositoryCustom.findAllProductsWithCursor(createdBy, size, ProductSort.NAME_DESC, cursor)
                    data.size shouldBe size
                    data.first().name shouldBe "sortTestp"
                }
            }
        }

        Given("상품 검색 요청 시") {
            val products = listOf(
                baseProductBuild {
                    name = "아메리카노"
                    createdAt = LocalDateTime.of(2024, 1, 1, 0, 0, 0)
                },
                baseProductBuild {
                    name = "슈크림 라떼"
                    createdAt = LocalDateTime.of(2024, 1, 2, 0, 0, 2)
                },
                baseProductBuild {
                    name = "자바칩 프라푸치노"
                    createdAt = LocalDateTime.of(2024, 1, 2, 0, 0, 3)
                },
                baseProductBuild {
                    name = "카페 모카"
                    createdAt = LocalDateTime.of(2024, 1, 2, 0, 0, 4)
                },
                baseProductBuild {
                    name = "카페 라떼"
                    createdAt = LocalDateTime.of(2024, 1, 2, 0, 0, 5)
                },
            )

            val consonants = listOf(
                baseConsonantBuild {
                    name = "아메리카노"
                    frontConsonant = "ㅇㅁㄹㅋㄴ"
                },
                baseConsonantBuild {
                    name = "슈크림 라떼"
                    frontConsonant = "ㅅㅋㄹ ㄹㄸ"
                },
                baseConsonantBuild {
                    name = "자바칩 프라푸치노"
                    frontConsonant = "ㅈㅂㅊ ㅍㄹㅍㅊㄴ"
                },
                baseConsonantBuild {
                    name = "카페 모카"
                    frontConsonant = "ㅋㅍ ㅁㅋ"
                },
                baseConsonantBuild {
                    name = "카페 라떼"
                    frontConsonant = "ㅋㅍ ㄹㄸ"
                },
            )

            products.forEach {
                productRepository.save(it)
            }
            consonants.forEach {
                consonantRepository.save(it)
            }
            When("검색어가 포함된 상품이 존재하는 경우") {

                val data = productRepositoryCustom.searchProduct("카페")
                val data2 = productRepositoryCustom.searchProduct("ㅋㅍ")

                Then("검색어가 포함된 상품 목록이 조회된다.") {
                    data[0].name shouldBe "카페 라떼"
                    data[1].name shouldBe "카페 모카"
                    data2[0].name shouldBe "카페 라떼"
                    data2[1].name shouldBe "카페 모카"
                }
            }
            When("검색어가 포함된 상품이 존재하지 않는 경우") {
                Then("빈 목록이 조회된다.") {
                    productRepositoryCustom.searchProduct("없는상품").size shouldBe 0
                }
            }
        }
    }
}
