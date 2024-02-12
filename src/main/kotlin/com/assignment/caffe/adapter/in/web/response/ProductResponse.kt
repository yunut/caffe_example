package com.assignment.caffe.adapter.`in`.web.response

import com.assignment.caffe.application.domain.model.Product
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.time.LocalDateTime

data class GetProductResponse(
    val product: GetProductBody,
) {
    companion object {
        fun toResponse(
            product: Product,
        ): GetProductResponse {
            return GetProductResponse(
                product = GetProductBody(
                    id = product.barCode.toString(),
                    category = product.category,
                    salePrice = product.salePrice,
                    originPrice = product.originPrice,
                    name = product.name,
                    description = product.description,
                    expireDate = product.expireDate,
                    size = product.size,
                    createdAt = product.createdAt!!,
                    updatedAt = product.updatedAt!!,
                ),
            )
        }
    }
}

data class GetProductBody(
    val id: String,
    val category: String,
    val salePrice: Int,
    val originPrice: Int,
    val name: String,
    val description: String,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val expireDate: LocalDate,
    val size: String,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val updatedAt: LocalDateTime,
)

data class GetProductListResponse(
    val products: List<GetProductListBody>,
) {
    companion object {
        fun toResponse(
            products: List<Product>,
        ): GetProductListResponse {
            return GetProductListResponse(
                products = products.map {
                    GetProductListBody(
                        id = it.barCode.toString(),
                        category = it.category,
                        salePrice = it.salePrice,
                        originPrice = it.originPrice,
                        name = it.name,
                        expireDate = it.expireDate,
                        size = it.size,
                        createdAt = it.createdAt!!,
                        updatedAt = it.updatedAt!!,
                    )
                },
            )
        }
    }
}

data class GetProductListBody(
    val id: String,
    val category: String,
    val salePrice: Int,
    val originPrice: Int,
    val name: String,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val expireDate: LocalDate,
    val size: String,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val updatedAt: LocalDateTime,
)
