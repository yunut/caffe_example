package com.assignment.caffe.application.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne

@Entity
class UserRefreshToken(
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    val user: User,
    private var refreshToken: String,
) {
    @Id
    @Column(name = "user_id")
    val userId: Int? = null

    @Column
    private var reissueCount = 0

    fun updateRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }

    fun validateRefreshToken(refreshToken: String): Boolean {
        return this.refreshToken == refreshToken
    }

    fun increaseReissueCount() {
        reissueCount++
    }
}
