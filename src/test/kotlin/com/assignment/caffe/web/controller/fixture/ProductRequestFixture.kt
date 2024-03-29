package com.assignment.caffe.web.controller.fixture

import com.assignment.caffe.adapter.`in`.web.request.CreateProductRequest
import com.assignment.caffe.adapter.`in`.web.request.UpdateProductRequest
import com.assignment.caffe.application.domain.enumeration.ProductSize
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

inline fun createProductRequestBuild(block: CreateProductRequestBuilder.() -> Unit = {}) =
    CreateProductRequestBuilder().apply(block).build()
class CreateProductRequestBuilder {
    var category: String = "카테고리"
    var salePrice: Int = 10000
    var originPrice: Int = 5000
    var name: String = "상품명"
    var description: String = "상품 설명"

    @JsonFormat(pattern = "yyyy-MM-dd")
    var expireDate: LocalDate = LocalDate.now()
    val size: ProductSize = ProductSize.SMALL
    fun build(): CreateProductRequest = CreateProductRequest(
        category = category,
        salePrice = salePrice,
        originPrice = originPrice,
        name = name,
        description = description,
        expireDate = expireDate,
        size = size,
    )
}

inline fun updateProductRequestBuild(block: UpdateProductRequestBuilder.() -> Unit = {}) =
    UpdateProductRequestBuilder().apply(block).build()
class UpdateProductRequestBuilder {
    var category: String? = null
    var salePrice: Int? = null
    var originPrice: Int? = null
    var name: String? = null
    var description: String? = null

    @JsonFormat(pattern = "yyyy-MM-dd")
    var expireDate: LocalDate? = null
    val size: ProductSize? = null
    fun build(): UpdateProductRequest = UpdateProductRequest(
        category = category,
        salePrice = salePrice,
        originPrice = originPrice,
        name = name,
        description = description,
        expireDate = expireDate,
        size = size,
    )
}
