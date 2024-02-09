package com.assignment.caffe.adapter.`in`.web.security.jwt

import com.assignment.caffe.adapter.`in`.web.security.jwt.config.JwtConfig
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Date
import javax.crypto.spec.SecretKeySpec

@Component
class JwtProvider(
    private val jwtConfig: JwtConfig,
) {

    // TODO Refresh 토큰 생성
    fun generateToken(userSpecification: String): String {
        return Jwts.builder()
            .signWith(SecretKeySpec(jwtConfig.secret.toByteArray(), SignatureAlgorithm.HS512.jcaName)) // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
            .setSubject(userSpecification)
            .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
            .setExpiration(Date.from(Instant.now().plus(jwtConfig.expirationHour.toLong(), ChronoUnit.HOURS)))    // JWT 토큰의 만료시간 설정
            .compact()!!
    }

    fun validateTokenAndGetSubject(token: String): String? {
        return Jwts.parserBuilder()
            .setSigningKey(jwtConfig.secret.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }
}