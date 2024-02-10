package com.assignment.caffe.adapter.out.persistence.repository

import com.assignment.caffe.application.domain.model.UserToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserTokenRepository : JpaRepository<UserToken, Long> {

    fun findByUserId(id: Int): UserToken?

    fun findByUserIdAndReissueCountLessThan(id: Int, count: Long): UserToken?

    fun deleteByUserId(id: Int)
}
