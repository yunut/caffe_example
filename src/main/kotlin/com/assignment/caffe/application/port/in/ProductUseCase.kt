package com.assignment.caffe.application.port.`in`

import com.assignment.caffe.application.port.`in`.query.CreateProductQuery

interface ProductUseCase {

    fun createProduct(createProductQuery: CreateProductQuery)
}
