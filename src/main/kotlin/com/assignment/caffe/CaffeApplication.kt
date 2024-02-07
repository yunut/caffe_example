package com.assignment.caffe

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CaffeApplication

fun main(args: Array<String>) {
    runApplication<CaffeApplication>(*args)
}
