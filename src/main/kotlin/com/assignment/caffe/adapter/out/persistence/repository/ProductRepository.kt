package com.assignment.caffe.adapter.out.persistence.repository

import com.assignment.caffe.application.domain.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ProductRepository : JpaRepository<Product, UUID>, ProductRepositoryCustom {

    fun existsByName(name: String): Boolean

    fun existsByBarCode(barCode: UUID): Boolean

    fun findProductByBarCode(barCode: UUID): Product?

    fun findProductByBarCodeAndCreatedBy(barCode: UUID, createdBy: String): Product?

    fun deleteByBarCode(barCode: UUID)
}
