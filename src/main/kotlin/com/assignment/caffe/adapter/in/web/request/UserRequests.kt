package com.assignment.caffe.adapter.`in`.web.request


import jakarta.validation.constraints.Pattern

data class SignUpRequest(
    @field:Pattern(
        regexp = "^\\d{3}-\\d{4}-\\d{4}\$",
        message = "전화번호 형식이 올바르지 않습니다.",
    )
    val phoneNumber: String,
    @field:Pattern(
        regexp = "^[a-zA-Z0-9]{8,16}\$",
        message = "비밀번호는 영문, 숫자로 8자리 이상 16자리 이하로 입력해주세요.",
    )
    val password: String,
)

data class SignInRequest(
    @field:Pattern(
        regexp = "^\\d{3}-\\d{4}-\\d{4}\$",
        message = "전화번호 형식이 올바르지 않습니다.",
    )
    val phoneNumber: String,
    @field:Pattern(
        regexp = "^[a-zA-Z0-9]{8,16}\$",
        message = "비밀번호는 영문, 숫자로 8자리 이상 16자리 이하로 입력해주세요.",
    )
    val password: String,
)
