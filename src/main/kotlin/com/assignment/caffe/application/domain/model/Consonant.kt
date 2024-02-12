package com.assignment.caffe.application.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "consonant")
class Consonant private constructor(

    @Id
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
