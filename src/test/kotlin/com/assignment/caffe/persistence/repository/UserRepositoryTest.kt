package com.assignment.caffe.persistence.repository

import com.assignment.caffe.adapter.out.persistence.repository.UserRepository
import com.assignment.caffe.application.domain.enum.UserRole
import com.assignment.caffe.application.domain.model.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest(
    @Autowired private val userRepository: UserRepository,
) : BehaviorSpec() {

    init {

        Given("유저 저장 요청 시") {
            When("phone_number가 중복되지 않는 경우") {

                val user1 = User.of("010-8989-8989", "test", listOf(UserRole.ROLE_USER))
                val user2 = User.of("010-8989-8990", "test", listOf(UserRole.ROLE_USER))
                Then("정상적으로 종료 된다.") {
                    withContext(Dispatchers.IO) {
                        userRepository.save(user1)
                        userRepository.save(user2)
                    }
                }
            }
            When("phone_number가 중복되는 경우") {

                val user1 = User.of("010-9090-9090", "test", listOf(UserRole.ROLE_USER))
                val user2 = User.of("010-9090-9090", "test", listOf(UserRole.ROLE_USER))

                Then("DataIntegrityViolationException이 발생한다.") {
                    shouldThrow<DataIntegrityViolationException> {
                        withContext(Dispatchers.IO) {
                            userRepository.save(user1)
                            userRepository.save(user2)
                        }
                    }
                }
            }
        }
    }
}
