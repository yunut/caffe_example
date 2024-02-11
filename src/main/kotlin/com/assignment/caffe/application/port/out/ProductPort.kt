package com.assignment.caffe.application.port.out

import com.assignment.caffe.application.domain.enumeration.ProductSort
import com.assignment.caffe.application.domain.model.Product

interface ProductPort {

    fun insertProduct(product: Product)

    fun updateProduct(product: Product)

    fun existsProductByName(name: String): Boolean

    fun existsProductById(id: String): Boolean

    fun findProductById(id: String): Product?

    fun deleteProduct(id: String)

    fun getProduct(productId: String, userId: String): Product?

    fun getProductsWithCursor(userId: String, size: Int, sort: ProductSort, cursor: String?): List<Product>

    fun searchProduct(keyword: String): List<Product>
}
