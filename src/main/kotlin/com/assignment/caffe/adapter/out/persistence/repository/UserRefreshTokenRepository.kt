package com.assignment.caffe.adapter.out.persistence.repository

import com.assignment.caffe.application.domain.model.UserRefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRefreshTokenRepository : JpaRepository<UserRefreshToken, Long> {

    fun findByUserId(id: Int): UserRefreshToken?
}
