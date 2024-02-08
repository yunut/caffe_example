package com.assignment.caffe.adapter.`in`.web.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val entryPoint: AuthenticationEntryPoint,
) {
    @Bean
    fun filterChain(http: HttpSecurity) = http
        .csrf { it.disable() }
        .authorizeHttpRequests {
            it.requestMatchers("/", "/signup", "/error").permitAll()	// requestMatchers의 인자로 전달된 url은 모두에게 허용
                .anyRequest().authenticated()	// 그 외의 모든 요청은 인증 필요
        }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }	// 세션을 사용하지 않으므로 STATELESS 설정
        .exceptionHandling { it.authenticationEntryPoint(entryPoint) }
        .build()!!

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}