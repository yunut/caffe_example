package com.assignment.caffe.adapter.`in`.web

import com.assignment.caffe.adapter.`in`.web.request.CreateProductRequest
import com.assignment.caffe.adapter.`in`.web.request.UpdateProductRequest
import com.assignment.caffe.adapter.`in`.web.response.GetProductListResponse
import com.assignment.caffe.adapter.`in`.web.response.GetProductResponse
import com.assignment.caffe.adapter.`in`.web.response.MetaBody
import com.assignment.caffe.adapter.`in`.web.response.ResponseBody
import com.assignment.caffe.application.domain.enumeration.ProductSort
import com.assignment.caffe.application.port.`in`.ProductUseCase
import com.assignment.caffe.application.port.`in`.query.CreateProductQuery
import com.assignment.caffe.application.port.`in`.query.UpdateProductQuery
import jakarta.validation.constraints.Max
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
    ): ResponseBody {
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
        return ResponseBody(MetaBody(HttpStatus.CREATED.value(), "Product create successfully"))
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    fun updateProduct(
        @PathVariable id: String,
        @Validated @RequestBody
        request: UpdateProductRequest,
    ): ResponseBody {
        val authentication = SecurityContextHolder.getContext().authentication

        val updateProductQuery = UpdateProductQuery(
            id = id,
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
        return ResponseBody(MetaBody(HttpStatus.OK.value(), "Product updated successfully"))
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}")
    fun deleteProduct(
        @PathVariable id: String,
    ): ResponseBody {
        productUseCase.deleteProduct(id)
        return ResponseBody(MetaBody(HttpStatus.OK.value(), "Product deleted successfully"))
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    fun getProduct(
        @PathVariable id: String,
    ): ResponseBody {
        val authentication = SecurityContextHolder.getContext().authentication

        val product = productUseCase.getProduct(id, authentication.name)
        return ResponseBody(MetaBody(HttpStatus.OK.value(), "Product retrieved successfully"), GetProductResponse.toResponse(product))
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list")
    fun getProductList(
        @RequestParam(required = false) cursor: String? = null,
        @RequestParam(required = false, defaultValue = "10")
        @Max(100)
        size: Int,
        @RequestParam(required = false, defaultValue = "CREATED_AT_DESC") sort: ProductSort,
    ): ResponseBody {
        val authentication = SecurityContextHolder.getContext().authentication

        val products = productUseCase.getProductsWithCursor(authentication.name, size, sort, cursor)
        return ResponseBody(MetaBody(HttpStatus.OK.value(), "Product list retrieved successfully"), GetProductListResponse.toResponse(products))
    }
}
