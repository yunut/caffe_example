package com.assignment.caffe.persistence.repository.fixture

import com.assignment.caffe.application.domain.enumeration.ProductSize
import com.assignment.caffe.application.domain.model.Product
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.util.UUID

inline fun baseProductBuild(block: BaseProductBuilder.() -> Unit = {}) =
    BaseProductBuilder().apply(block).build()
class BaseProductBuilder {

    var barCode: UUID = UUID.randomUUID()
    var category: String = "category"
    var salePrice: Int = 10000
    var originPrice: Int = 5000
    var name: String = "product name"
    var description: String = "product description"

    @JsonFormat(pattern = "yyyy-MM-dd")
    var expireDate: LocalDate = LocalDate.now()
    val size: ProductSize = ProductSize.SMALL
    val createdBy = "user id"

    fun build(): Product = Product.of(
        barCode = barCode,
        category = category,
        salePrice = salePrice,
        originPrice = originPrice,
        name = name,
        description = description,
        expireDate = expireDate,
        size = size,
        createdBy = createdBy,
    )
}
