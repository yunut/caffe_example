package com.assignment.caffe.application.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "phone_number")
    val phoneNumber: String,

    @Column(name = "password")
    val password: String
) {
    companion object {
        fun of(phoneNumber: String, password: String): User {
            return User(0, phoneNumber, password)
        }
    }
}