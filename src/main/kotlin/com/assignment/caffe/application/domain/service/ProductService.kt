package com.assignment.caffe.application.domain.service

import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.port.`in`.ProductUseCase
import com.assignment.caffe.application.port.`in`.query.CreateProductQuery
import com.assignment.caffe.application.port.`in`.query.toEntity
import com.assignment.caffe.application.port.out.ProductPort
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productPort: ProductPort,
) : ProductUseCase {

    override fun createProduct(createProductQuery: CreateProductQuery) {
        val product = createProductQuery.toEntity()

        if (productPort.existsProductByName(product.name)) {
            throw ConflictException("Product with name ${product.name} already exists")
        }

        productPort.insertProduct(product)
    }
}
