package com.assignment.caffe.application.domain.model

import com.assignment.caffe.application.domain.enumeration.UserRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.UUID

@Entity
@Table(name = "user")
class User private constructor(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val id: UUID? = null,

    @Column(name = "phone_number")
    val phoneNumber: String,

    @Column(name = "password")
    val password: String,

    @Column(name = "roles")
    val roles: String,
) {
    companion object {
        fun of(phoneNumber: String, password: String, userRoles: List<UserRole>, id: UUID? = null): User {
            return User(
                phoneNumber = phoneNumber,
                password = password,
                roles = userRoles.joinToString(",") { it.name },
                id = id,
            )
        }
    }
}
