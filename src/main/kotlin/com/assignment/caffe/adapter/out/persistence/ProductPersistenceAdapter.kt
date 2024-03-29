package com.assignment.caffe.adapter.out.persistence

import com.assignment.caffe.adapter.out.persistence.repository.ConsonantRepository
import com.assignment.caffe.adapter.out.persistence.repository.ProductRepository
import com.assignment.caffe.adapter.out.persistence.util.textFarsing
import com.assignment.caffe.application.domain.enumeration.ProductSort
import com.assignment.caffe.application.domain.model.Consonant
import com.assignment.caffe.application.domain.model.Product
import com.assignment.caffe.application.port.out.ProductPort
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProductPersistenceAdapter(
    private val productRepository: ProductRepository,
    private val consonantRepository: ConsonantRepository,
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

    override fun deleteProduct(id: String) {
        productRepository.deleteByBarCode(UUID.fromString(id))
    }

    override fun getProduct(productId: String, userId: String): Product? {
        return productRepository.findProductByBarCodeAndCreatedBy(UUID.fromString(productId), userId)
    }

    override fun getProductsWithCursor(userId: String, size: Int, sort: ProductSort, cursor: String?): List<Product> {
        return productRepository.findAllProductsWithCursor(userId, size, sort, cursor)
    }

    override fun searchProduct(keyword: String): List<Product> {
        return productRepository.searchProduct(keyword)
    }

    override fun saveConsonant(product: Product) {
        val consonant = textFarsing(product.name)

        consonantRepository.save(Consonant.of(product.name, consonant))
    }
}
