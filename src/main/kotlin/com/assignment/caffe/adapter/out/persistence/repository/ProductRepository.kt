package com.assignment.caffe.adapter.out.persistence.repository

import com.assignment.caffe.application.domain.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, String> {

    fun existsByName(name: String): Boolean
}
