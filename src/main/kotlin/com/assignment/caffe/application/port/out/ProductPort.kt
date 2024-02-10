package com.assignment.caffe.application.port.out

import com.assignment.caffe.application.domain.model.Product

interface ProductPort {

    fun insertProduct(product: Product)

    fun existsProductByName(name: String): Boolean
}
