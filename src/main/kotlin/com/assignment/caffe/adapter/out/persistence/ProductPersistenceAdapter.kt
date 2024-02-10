package com.assignment.caffe.adapter.out.persistence

import com.assignment.caffe.adapter.out.persistence.repository.ProductRepository
import com.assignment.caffe.application.domain.model.Product
import com.assignment.caffe.application.port.out.ProductPort
import org.springframework.stereotype.Component

@Component
class ProductPersistenceAdapter(
    private val productRepository: ProductRepository,
) : ProductPort {
    override fun insertProduct(product: Product) {
        productRepository.save(product)
    }

    override fun existsProductByName(name: String): Boolean {
        return productRepository.existsByName(name)
    }
}
