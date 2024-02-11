package com.assignment.caffe.adapter.out.persistence.repository

import com.assignment.caffe.application.domain.enumeration.ProductSort
import com.assignment.caffe.application.domain.model.Product

interface ProductRepositoryCustom {

    fun findAllProductsWithCursor(userId: String, size: Int, sort: ProductSort, cursor: String?): List<Product>

    fun searchProduct(keyword: String): List<Product>
}
