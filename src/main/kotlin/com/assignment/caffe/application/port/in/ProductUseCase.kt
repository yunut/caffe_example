package com.assignment.caffe.application.port.`in`

import com.assignment.caffe.application.port.`in`.query.CreateProductQuery
import com.assignment.caffe.application.port.`in`.query.UpdateProductQuery

interface ProductUseCase {

    fun createProduct(createProductQuery: CreateProductQuery)

    fun updateProduct(updateProductQuery: UpdateProductQuery)
}
