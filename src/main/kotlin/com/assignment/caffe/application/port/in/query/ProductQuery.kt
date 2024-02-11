package com.assignment.caffe.application.port.`in`.query

import com.assignment.caffe.application.domain.enumeration.ProductSize
import java.time.LocalDate

data class CreateProductQuery(
    val category: String,
    val salePrice: Int,
    val originPrice: Int,
    val name: String,
    val description: String,
    val expireDate: LocalDate,
    val size: ProductSize,
    val createdBy: String,
)

data class UpdateProductQuery(
    val id: String,
    val category: String? = null,
    val salePrice: Int? = null,
    val originPrice: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val expireDate: LocalDate? = null,
    val size: ProductSize? = null,
    val createdBy: String? = null,
)
