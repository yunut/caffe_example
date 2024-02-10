package com.assignment.caffe.adapter.`in`.web.exception

import org.springframework.http.HttpStatus

enum class WebErrors(val httpStatus: HttpStatus, val defaultMessage: String) {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST"),
    MISSING_HEADER(HttpStatus.BAD_REQUEST, "MISSING HEADER"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "MISSING_PARAMETER"),
    CONFLICT(HttpStatus.CONFLICT, "CONFLICT"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "ACCESS DENIED"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT FOUND"),

    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "UNKNOWN ERROR"),
}
