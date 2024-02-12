package com.assignment.caffe.application.domain.model

import com.assignment.caffe.application.domain.enumeration.ProductSize
import com.assignment.caffe.application.port.`in`.query.UpdateProductQuery
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "product")
class Product private constructor(

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    val barCode: UUID? = UUID.randomUUID(),

    @Column(name = "category")
    val category: String,

    @Column(name = "sale_price")
    val salePrice: Int,

    @Column(name = "origin_price")
    val originPrice: Int,

    @Column(name = "name")
    val name: String,

    @Column(name = "description")
    val description: String,

    @Column(name = "expire_date")
    val expireDate: LocalDate,

    @Column(name = "size")
    val size: String,

    @Column(name = "created_by")
    val createdBy: String,

    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null,

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null,
) {

    companion object {
        fun of(
            category: String,
            salePrice: Int,
            originPrice: Int,
            name: String,
            description: String,
            expireDate: LocalDate,
            size: ProductSize,
            createdBy: String,
            barCode: UUID? = null,
            createdAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null,
        ): Product {
            return Product(
                category = category,
                salePrice = salePrice,
                originPrice = originPrice,
                name = name,
                description = description,
                expireDate = expireDate,
                size = size.name,
                createdBy = createdBy,
                barCode = barCode,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }
    }

    fun update(
        updateProductQuery: UpdateProductQuery,
    ): Product {
        return Product(
            category = updateProductQuery.category ?: this.category,
            salePrice = updateProductQuery.salePrice ?: this.salePrice,
            originPrice = updateProductQuery.originPrice ?: this.originPrice,
            name = updateProductQuery.name ?: this.name,
            description = updateProductQuery.description ?: this.description,
            expireDate = updateProductQuery.expireDate ?: this.expireDate,
            size = updateProductQuery.size?.name ?: this.size,
            createdBy = updateProductQuery.createdBy ?: this.createdBy,
            barCode = this.barCode,
        )
    }
}
