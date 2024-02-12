package com.assignment.caffe.application.port.`in`

import com.assignment.caffe.application.domain.enumeration.ProductSort
import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.domain.exception.NotFoundException
import com.assignment.caffe.application.domain.model.Product
import com.assignment.caffe.application.port.`in`.query.CreateProductQuery
import com.assignment.caffe.application.port.`in`.query.UpdateProductQuery

interface ProductUseCase {

    @Throws(ConflictException::class)
    fun createProduct(createProductQuery: CreateProductQuery): String

    @Throws(NotFoundException::class)
    fun updateProduct(updateProductQuery: UpdateProductQuery)

    fun deleteProduct(id: String)

    fun getProduct(productId: String, userId: String): Product

    fun getProductsWithCursor(userId: String, size: Int, sort: ProductSort, cursor: String?): List<Product>

    fun searchProduct(keyword: String): List<Product>
}
