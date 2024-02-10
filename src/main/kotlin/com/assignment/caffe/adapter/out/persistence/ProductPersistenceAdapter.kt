package com.assignment.caffe.adapter.out.persistence

import com.assignment.caffe.adapter.out.persistence.repository.ProductRepository
import com.assignment.caffe.application.domain.model.Product
import com.assignment.caffe.application.port.out.ProductPort
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProductPersistenceAdapter(
    private val productRepository: ProductRepository,
) : ProductPort {
    override fun insertProduct(product: Product) {
        productRepository.save(product)
    }

    override fun updateProduct(product: Product) {
        productRepository.save(product)
    }

    override fun existsProductByName(name: String): Boolean {
        return productRepository.existsByName(name)
    }

    override fun existsProductById(id: String): Boolean {
        return productRepository.existsByBarCode(UUID.fromString(id))
    }

    override fun findProductById(id: String): Product? {
        return productRepository.findProductByBarCode(UUID.fromString(id))
    }
}
