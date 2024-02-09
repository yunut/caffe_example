package com.assignment.caffe.adapter.`in`.web

import com.assignment.caffe.adapter.`in`.web.request.SignInRequest
import com.assignment.caffe.adapter.`in`.web.request.SignUpRequest
import com.assignment.caffe.adapter.`in`.web.response.MetaBody
import com.assignment.caffe.adapter.`in`.web.response.ResponseBody
import com.assignment.caffe.adapter.`in`.web.response.SignInResponse
import com.assignment.caffe.application.port.`in`.SignUseCase
import com.assignment.caffe.application.port.`in`.query.SignInQuery
import com.assignment.caffe.application.port.`in`.query.SignUpQuery
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class SignController(
    private val signUseCase: SignUseCase,
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    fun signUp(
        @Validated @RequestBody
        request: SignUpRequest,
    ): ResponseBody {
        val signUpQuery = SignUpQuery.of(
            request.phoneNumber,
            request.password,
        )
        signUseCase.signUp(signUpQuery)
        return ResponseBody(MetaBody(HttpStatus.CREATED.value(), "User signed up successfully"))
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/signin")
    fun signIn(
        @Valid @RequestBody
        request: SignInRequest,
    ): ResponseBody {
        val signInQuery = SignInQuery.of(
            request.phoneNumber,
            request.password,
        )

        val data = signUseCase.signIn(signInQuery)
        return ResponseBody(MetaBody(HttpStatus.OK.value(), "User signed in successfully"), SignInResponse(data.accessToken, data.refreshToken))
    }
}
