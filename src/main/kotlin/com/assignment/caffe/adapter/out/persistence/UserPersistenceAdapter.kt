package com.assignment.caffe.adapter.out.persistence

import com.assignment.caffe.adapter.out.persistence.repository.UserRepository
import com.assignment.caffe.application.domain.model.User
import com.assignment.caffe.application.port.out.UserPort
import org.springframework.stereotype.Component

@Component
class UserPersistenceAdapter(
    private val userRepository: UserRepository
): UserPort {
    override fun insertUser(user: User) {
        userRepository.save(user)
    }

    override fun existsUserByPhoneNumber(phoneNumber: String): Boolean {
        return userRepository.existsByPhoneNumber(phoneNumber)
    }

}