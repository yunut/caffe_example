package com.assignment.caffe.application.domain.model

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.UUID

@Entity
@Table(name = "consonant")
class Consonant private constructor(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val id: UUID? = UUID.randomUUID(),

    @Column
    val name: String,

    @Column
    val frontConsonant: String,
) {
    companion object {
        fun of(name: String, frontConsonant: String): Consonant {
            return Consonant(name = name, frontConsonant = frontConsonant)
        }
    }
}
