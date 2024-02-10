package com.assignment.caffe.application.domain.model

import com.assignment.caffe.application.domain.enum.UserRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user")
class User private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "phone_number")
    val phoneNumber: String,

    @Column(name = "password")
    val password: String,

    @Column(name = "roles")
    val roles: String,
) {
    companion object {
        fun of(phoneNumber: String, password: String, userRoles: List<UserRole>, id: Int? = null): User {
            return User(
                phoneNumber = phoneNumber,
                password = password,
                roles = userRoles.joinToString(",") { it.name },
                id = id,
            )
        }
    }
}
