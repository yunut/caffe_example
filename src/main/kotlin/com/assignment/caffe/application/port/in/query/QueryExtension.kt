package com.assignment.caffe.application.port.`in`.query

import com.assignment.caffe.application.domain.enum.UserRole
import com.assignment.caffe.application.domain.model.Product
import com.assignment.caffe.application.domain.model.User
import org.springframework.security.crypto.password.PasswordEncoder

fun SignUpQuery.toEntity(passwordEncoder: PasswordEncoder, roles: List<UserRole>) = User.of(
    phoneNumber = this.phoneNumber,
    password = passwordEncoder.encode(this.password),
    userRoles = roles,
)

fun CreateProductQuery.toEntity() = Product.of(
    category = this.category,
    salePrice = this.salePrice,
    originPrice = this.originPrice,
    name = this.name,
    description = this.description,
    expireDate = this.expireDate,
    size = this.size,
    createdBy = this.createdBy,
)
