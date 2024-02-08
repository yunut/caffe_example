package com.assignment.caffe.adapter.`in`.web.exception

import com.assignment.caffe.adapter.`in`.web.response.MetaBody
import com.assignment.caffe.adapter.`in`.web.response.ResponseBody
import com.assignment.caffe.application.domain.exception.ConflictException
import jakarta.servlet.http.HttpServletResponse
import org.hibernate.exception.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.validation.FieldError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, response: HttpServletResponse): ResponseEntity<ResponseBody> {
        val body: ResponseBody
        val httpStatus: HttpStatus

        when (e) {
            is MethodArgumentTypeMismatchException -> {
                body = ResponseBody(MetaBody(WebErrors.BAD_REQUEST.httpStatus.value(), e.message ?: "No Message"), null)
                httpStatus = WebErrors.BAD_REQUEST.httpStatus
            }
            is MethodArgumentNotValidException -> {
                val fieldError: FieldError? = e.bindingResult.fieldError
                body = ResponseBody(MetaBody(WebErrors.BAD_REQUEST.httpStatus.value(), fieldError?.defaultMessage ?: "No Message"), null)
                httpStatus = WebErrors.BAD_REQUEST.httpStatus
            }
            is MissingServletRequestParameterException -> {
                body = ResponseBody(
                    MetaBody(
                        WebErrors.INVALID_PARAMETER.httpStatus.value(),
                        "'${e.parameterName}' parameter is missing or null",
                    ),
                    null,
                )
                httpStatus = WebErrors.BAD_REQUEST.httpStatus
            }
            is MissingRequestHeaderException -> {
                body = ResponseBody(MetaBody(WebErrors.MISSING_HEADER.httpStatus.value(), e.message), null)
                httpStatus = WebErrors.MISSING_HEADER.httpStatus
            }
            is ConstraintViolationException -> {
                body = ResponseBody(MetaBody(WebErrors.BAD_REQUEST.httpStatus.value(), e.message ?: "No Message"), null)
                httpStatus = WebErrors.BAD_REQUEST.httpStatus
            }
            is HttpRequestMethodNotSupportedException -> {
                body = ResponseBody(MetaBody(WebErrors.BAD_REQUEST.httpStatus.value(), e.message ?: "No Message"), null)
                httpStatus = WebErrors.BAD_REQUEST.httpStatus
            }
            is HttpMessageNotReadableException -> {
                body = ResponseBody(MetaBody(WebErrors.BAD_REQUEST.httpStatus.value(), e.message ?: "No Message"), null)
                httpStatus = WebErrors.BAD_REQUEST.httpStatus
            }
            is ConflictException -> {
                body = ResponseBody(MetaBody(WebErrors.CONFLICT.httpStatus.value(), e.message ?: "No Message"), null)
                httpStatus = WebErrors.CONFLICT.httpStatus
            }
            is AccessDeniedException -> {
                body = ResponseBody(MetaBody(WebErrors.ACCESS_DENIED.httpStatus.value(), e.message ?: "No Message"), null)
                httpStatus = WebErrors.ACCESS_DENIED.httpStatus
            }
            else -> {
                body = ResponseBody(MetaBody(WebErrors.UNKNOWN_ERROR.httpStatus.value(), "서버 관리자에게 문의하세요"), null)
                httpStatus = WebErrors.UNKNOWN_ERROR.httpStatus
            }
        }

        return ResponseEntity(body, httpStatus)
    }
}
