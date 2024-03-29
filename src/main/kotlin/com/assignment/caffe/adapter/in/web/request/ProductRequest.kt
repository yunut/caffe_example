package com.assignment.caffe.adapter.`in`.web.request

import com.assignment.caffe.application.domain.enumeration.ProductSize
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.time.LocalDate

data class CreateProductRequest(

    @field:NotBlank
    @field:NotNull
    val category: String,

    // 0보다 큰 정수
    @field:NotNull
    @field:Min(1)
    val salePrice: Int,

    // 0을 포함한 양의 정수
    @field:NotNull
    @field:Min(0)
    val originPrice: Int,

    @field:NotBlank
    @field:NotNull
    @field:Pattern(regexp = "^.{0,100}\$")
    val name: String,

    @field:NotNull
    @field:Pattern(regexp = "^.{0,1000}\$")
    val description: String,

    @field:NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    val expireDate: LocalDate,

    @field:NotNull
    val size: ProductSize,
)

data class UpdateProductRequest(

    @field:Pattern(regexp = "^.{1,}\$")
    val category: String? = null,

    // 0보다 큰 정수
    @field:Min(1)
    val salePrice: Int? = null,

    // 0을 포함한 양의 정수
    @field:Min(0)
    val originPrice: Int? = null,

    @field:Pattern(regexp = "^.{1,100}\$")
    val name: String? = null,

    @field:Pattern(regexp = "^.{1,1000}\$")
    val description: String? = null,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val expireDate: LocalDate? = null,

    val size: ProductSize? = null,
)
