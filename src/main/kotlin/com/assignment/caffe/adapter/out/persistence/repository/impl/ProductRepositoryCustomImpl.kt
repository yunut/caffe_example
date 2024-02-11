package com.assignment.caffe.adapter.out.persistence.repository.impl

import com.assignment.caffe.adapter.out.persistence.repository.ProductRepositoryCustom
import com.assignment.caffe.application.domain.enumeration.ProductSort
import com.assignment.caffe.application.domain.model.Product
import com.assignment.caffe.application.domain.model.QProduct.product
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class ProductRepositoryCustomImpl(
    private val query: JPAQueryFactory,
) : ProductRepositoryCustom {
    override fun findAllProductsWithCursor(
        userId: String,
        size: Int,
        sort: ProductSort,
        cursor: String?,
    ): List<Product> {
        val sortOrder = when (sort) {
            ProductSort.NAME_ASC -> product.name.asc()
            ProductSort.NAME_DESC -> product.name.desc()
            ProductSort.CREATED_AT_DESC -> product.createdAt.desc()
            ProductSort.CREATED_AT_ASC -> product.createdAt.asc()
            ProductSort.UPDATED_AT_DESC -> product.updatedAt.desc()
            ProductSort.UPDATED_AT_ASC -> product.updatedAt.asc()
        }

        val cursorField = when (sort) {
            ProductSort.NAME_DESC, ProductSort.NAME_ASC -> product.name
            ProductSort.UPDATED_AT_DESC, ProductSort.UPDATED_AT_ASC -> product.updatedAt
            else -> product.createdAt
        }

        val queryBuilder = query.select(product)
            .from(product)
            .where(product.createdBy.eq(userId))

        if (!cursor.isNullOrEmpty()) {
            queryBuilder.where(cursorField.stringValue().lt(cursor))
        }

        return queryBuilder.orderBy(sortOrder)
            .limit(size.toLong())
            .fetch()
    }
}
