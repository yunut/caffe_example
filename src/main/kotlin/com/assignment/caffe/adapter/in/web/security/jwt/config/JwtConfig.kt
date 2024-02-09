package com.assignment.caffe.adapter.`in`.web.security.jwt.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt.config")
class JwtConfig(
    val secret: String,
    val expirationHour: String,
    val refreshExpirationHour: String,
)
