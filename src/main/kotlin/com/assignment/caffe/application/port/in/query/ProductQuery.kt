package com.assignment.caffe.application.port.`in`.query

import com.assignment.caffe.application.domain.enum.ProductSize
import java.time.LocalDateTime

data class CreateProductQuery(
    val category: String,
    val salePrice: Int,
    val originPrice: Int,
    val name: String,
    val description: String,
    val expireDate: LocalDateTime,
    val size: ProductSize,
    val createdBy: String,
) {
    companion object {
        fun of(
            category: String,
            salePrice: Int,
            originPrice: Int,
            name: String,
            description: String,
            expireDate: LocalDateTime,
            size: ProductSize,
            createdBy: String,
        ): CreateProductQuery {
            return CreateProductQuery(
                category,
                salePrice,
                originPrice,
                name,
                description,
                expireDate,
                size,
                createdBy,
            )
        }
    }
}
