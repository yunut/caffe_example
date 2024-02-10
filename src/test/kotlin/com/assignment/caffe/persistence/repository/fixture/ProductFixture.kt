package com.assignment.caffe.persistence.repository.fixture

import com.assignment.caffe.application.domain.enum.ProductSize
import com.assignment.caffe.application.domain.model.Product
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

inline fun createProductBuild(block: CreateProductBuilder.() -> Unit = {}) =
    CreateProductBuilder().apply(block).build()
class CreateProductBuilder {
    var category: String = "category"
    var salePrice: Int = 10000
    var originPrice: Int = 5000
    var name: String = "product name"
    var description: String = "product description"

    @JsonFormat(pattern = "yyyy-MM-dd")
    var expireDate: LocalDate = LocalDate.now()
    val size: ProductSize = ProductSize.SMALL
    val createdBy = "user id"

    fun build(): Product = Product.of(
        category = category,
        salePrice = salePrice,
        originPrice = originPrice,
        name = name,
        description = description,
        expireDate = expireDate,
        size = size,
        createdBy = createdBy,
    )
}