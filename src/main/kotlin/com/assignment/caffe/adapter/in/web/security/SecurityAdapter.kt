package com.assignment.caffe.adapter.`in`.web.security

import com.assignment.caffe.application.port.out.AuthPort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class SecurityAdapter(
    private val passwordEncoder: PasswordEncoder
): AuthPort {
    override fun getEncryptedObject(): PasswordEncoder {
        return passwordEncoder
    }

}