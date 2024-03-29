package com.assignment.caffe.application.domain.service

import com.assignment.caffe.application.domain.enumeration.ProductSort
import com.assignment.caffe.application.domain.exception.ConflictException
import com.assignment.caffe.application.domain.exception.NotFoundException
import com.assignment.caffe.application.domain.model.Product
import com.assignment.caffe.application.port.`in`.ProductUseCase
import com.assignment.caffe.application.port.`in`.query.CreateProductQuery
import com.assignment.caffe.application.port.`in`.query.UpdateProductQuery
import com.assignment.caffe.application.port.`in`.query.toEntity
import com.assignment.caffe.application.port.out.ProductPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductService(
    private val productPort: ProductPort,
) : ProductUseCase {

    @Transactional
    override fun createProduct(createProductQuery: CreateProductQuery): String {
        val product = createProductQuery.toEntity()

        if (productPort.existsProductByName(product.name)) {
            throw ConflictException("Product with name ${product.name} already exists")
        }

        productPort.insertProduct(product)
        productPort.saveConsonant(product)

        return product.barCode.toString()
    }

    @Transactional
    override fun updateProduct(updateProductQuery: UpdateProductQuery) {
        if (!productPort.existsProductById(updateProductQuery.id)) {
            throw NotFoundException("Product with id ${updateProductQuery.id} not found")
        }

        val product = productPort.findProductById(updateProductQuery.id)!!.update(updateProductQuery)
        productPort.updateProduct(product)
        productPort.saveConsonant(product)
    }

    @Transactional
    override fun deleteProduct(id: String) {
        productPort.deleteProduct(id)
    }

    override fun getProduct(productId: String, userId: String): Product {
        return productPort.getProduct(productId, userId) ?: throw NotFoundException("Product with id $productId not found")
    }

    override fun getProductsWithCursor(userId: String, size: Int, sort: ProductSort, cursor: String?): List<Product> {
        return productPort.getProductsWithCursor(userId, size, sort, cursor)
    }

    override fun searchProduct(keyword: String): List<Product> {
        return productPort.searchProduct(keyword)
    }
}
