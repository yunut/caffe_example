package com.assignment.caffe.adapter.out.persistence.repository.impl

import com.assignment.caffe.adapter.out.persistence.repository.ProductRepositoryCustom
import com.assignment.caffe.application.domain.enumeration.ProductSort
import com.assignment.caffe.application.domain.model.Product
import com.assignment.caffe.application.domain.model.QConsonant
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

    override fun searchProduct(keyword: String): List<Product> {
        val query = query.selectFrom(product)

        // keyword가 완벽한 한글로 이루어진 경우를 검색하는 조건 추가
        if (keyword.matches(Regex("[가-힣]+"))) {
            query.where(product.name.like("%$keyword%"))
        } else {
            // keyword가 자음으로만 이루어진 경우를 검색하는 조건 추가
            if (keyword.matches(Regex("[ㄱ-ㅎ]+"))) {
                query.leftJoin(QConsonant.consonant)
                    .on(product.name.eq(QConsonant.consonant.name))
                    .where(QConsonant.consonant.frontConsonant.like("%$keyword%"))
            } else {
                // 그 외의 경우에는 일반적인 키워드 검색을 수행
                query.where(product.name.like("%$keyword%"))
            }
        }

        return query.fetch()
    }
}
