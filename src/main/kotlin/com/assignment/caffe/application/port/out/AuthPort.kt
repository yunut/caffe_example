package com.assignment.caffe.application.port.out

import org.springframework.security.crypto.password.PasswordEncoder

interface AuthPort {
    fun getEncryptedObject(): PasswordEncoder
}