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
    override fun createProduct(createProductQuery: CreateProductQuery) {
        val product = createProductQuery.toEntity()

        if (productPort.existsProductByName(product.name)) {
            throw ConflictException("Product with name ${product.name} already exists")
        }

        productPort.insertProduct(product)
    }

    @Transactional
    override fun updateProduct(updateProductQuery: UpdateProductQuery) {
        if (!productPort.existsProductById(updateProductQuery.id)) {
            throw NotFoundException("Product with id ${updateProductQuery.id} not found")
        }

        val product = productPort.findProductById(updateProductQuery.id)!!.update(updateProductQuery)
        productPort.updateProduct(product)
    }

    @Transactional
    override fun deleteProduct(id: String) {
        productPort.deleteProduct(id)
    }

    override fun getProduct(productId: String, userId: String): Product {
        return productPort.getProduct(productId, userId) ?: throw NotFoundException("Product with id $productId not found")
    }

    override fun getProductsWithCursor(userId: String, page: Int, size: Int, sort: ProductSort): List<Product> {
        TODO("Not yet implemented")
    }
}
