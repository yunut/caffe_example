package com.assignment.caffe.adapter.`in`.web.response

import com.assignment.caffe.application.domain.model.Product
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class GetProductResponse(
    val id: String,
    val category: String,
    val salePrice: Int,
    val originPrice: Int,
    val name: String,
    val description: String,
    @JsonFormat(pattern = "yyyy-MM-dd")
    val expireDate: LocalDate,
    val size: String,
) {
    companion object {
        fun toResponse(
            product: Product,
        ): GetProductResponse {
            return GetProductResponse(
                id = product.barCode.toString(),
                category = product.category,
                salePrice = product.salePrice,
                originPrice = product.originPrice,
                name = product.name,
                description = product.description,
                expireDate = product.expireDate,
                size = product.size,
            )
        }
    }
}

data class GetProductListResponse(
    val products: List<GetProductResponse>,
) {
    companion object {
        fun toResponse(
            products: List<Product>,
        ): List<GetProductListBody> {
            return products.map {
                GetProductListBody(
                    id = it.barCode.toString(),
                    category = it.category,
                    salePrice = it.salePrice,
                    originPrice = it.originPrice,
                    name = it.name,
                    expireDate = it.expireDate,
                    size = it.size,
                )
            }
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
)
