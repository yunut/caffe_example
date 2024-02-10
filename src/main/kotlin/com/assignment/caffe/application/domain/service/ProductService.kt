package com.assignment.caffe.application.domain.service

import com.assignment.caffe.application.port.`in`.ProductUseCase
import com.assignment.caffe.application.port.`in`.query.CreateProductQuery
import org.springframework.stereotype.Service

@Service
class ProductService() : ProductUseCase {

    override fun createProduct(createProductQuery: CreateProductQuery) {
        TODO()
    }
}
