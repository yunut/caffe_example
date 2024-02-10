package com.assignment.caffe.adapter.`in`.web

import com.assignment.caffe.adapter.`in`.web.request.CreateProductRequest
import com.assignment.caffe.adapter.`in`.web.request.UpdateProductRequest
import com.assignment.caffe.application.port.`in`.ProductUseCase
import com.assignment.caffe.application.port.`in`.query.CreateProductQuery
import com.assignment.caffe.application.port.`in`.query.UpdateProductQuery
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/product")
class ProductController(
    private val productUseCase: ProductUseCase,
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    fun createProduct(
        @Validated @RequestBody
        request: CreateProductRequest,
    ) {
        val authentication = SecurityContextHolder.getContext().authentication

        val createProductQuery = CreateProductQuery(
            category = request.category,
            salePrice = request.salePrice,
            originPrice = request.originPrice,
            name = request.name,
            description = request.description,
            expireDate = request.expireDate,
            size = request.size,
            createdBy = authentication.name,
        )

        productUseCase.createProduct(createProductQuery)
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("")
    fun updateProduct(
        @Validated @RequestBody
        request: UpdateProductRequest,
    ) {
        val authentication = SecurityContextHolder.getContext().authentication

        val updateProductQuery = UpdateProductQuery(
            id = authentication.name,
            category = request.category,
            salePrice = request.salePrice,
            originPrice = request.originPrice,
            name = request.name,
            description = request.description,
            expireDate = request.expireDate,
            size = request.size,
            createdBy = authentication.name,
        )

        productUseCase.updateProduct(updateProductQuery)
    }
}
