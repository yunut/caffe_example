package com.assignment.caffe.application.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne

@Entity
class UserToken(
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    val user: User,
    private var accessToken: String,
    private var refreshToken: String,
) {
    @Id
    @Column(name = "user_id")
    val userId: Int? = null

    @Column
    private var reissueCount = 0

    fun updateAccessToken(accessToken: String) {
        this.accessToken = accessToken
    }

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
