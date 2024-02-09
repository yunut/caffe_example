package com.assignment.caffe.application.domain.exception

class ConflictException(message: String) : RuntimeException(message)

class NotMatchException(message: String) : RuntimeException(message)