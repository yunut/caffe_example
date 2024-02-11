package com.assignment.caffe.application.port.`in`

import com.assignment.caffe.application.domain.enum.ProductSort
import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.domain.exception.NotFoundException
import com.assignment.caffe.application.domain.model.Product
import com.assignment.caffe.application.port.`in`.query.CreateProductQuery
import com.assignment.caffe.application.port.`in`.query.UpdateProductQuery

interface ProductUseCase {

    @Throws(ConflictException::class)
    fun createProduct(createProductQuery: CreateProductQuery)

    @Throws(NotFoundException::class)
    fun updateProduct(updateProductQuery: UpdateProductQuery)

    fun deleteProduct(id: String)

    fun getProduct(productId: String, userId: String): Product

    fun getProductsWithCursor(userId: String, page: Int, size: Int, sort: ProductSort): List<Product>
}
