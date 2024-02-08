package com.assignment.caffe.adapter.out.persistence.repository

import com.assignment.caffe.application.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
}