package com.assignment.caffe

import com.assignment.caffe.adapter.`in`.web.security.jwt.config.JwtConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    JwtConfig::class,
)
class CaffeApplication

fun main(args: Array<String>) {
    runApplication<CaffeApplication>(*args)
}
