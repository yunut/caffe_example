package com.assignment.caffe.adapter.`in`.web.security.jwt

import com.assignment.caffe.adapter.`in`.web.security.jwt.config.JwtConfig
import com.assignment.caffe.adapter.out.redis.repository.UserTokenRepository
import com.assignment.caffe.application.domain.model.UserRefreshToken
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Base64
import java.util.Date
import javax.crypto.spec.SecretKeySpec

@Component
class JwtProvider(
    private val jwtConfig: JwtConfig,
    private val objectMapper: ObjectMapper,
    private val userTokenRepository: UserTokenRepository, // 직접 참조 예외적 허용
) {

    fun generateAccessToken(userSpecification: String): String {
        return Jwts.builder()
            .signWith(SecretKeySpec(jwtConfig.secret.toByteArray(), SignatureAlgorithm.HS512.jcaName)) // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
            .setSubject(userSpecification)
            .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
            .setExpiration(Date.from(Instant.now().plus(jwtConfig.expirationHour.toLong(), ChronoUnit.HOURS))) // JWT 토큰의 만료시간 설정
            .compact()!!
    }

    fun generateRefreshToken(): String {
        return Jwts.builder()
            .signWith(SecretKeySpec(jwtConfig.secret.toByteArray(), SignatureAlgorithm.HS512.jcaName)) // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
            .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
            .setExpiration(Date.from(Instant.now().plus(jwtConfig.refreshExpirationHour.toLong(), ChronoUnit.HOURS)))
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

    @Transactional
    fun reGenerateAccessToken(oldAccessToken: String): String {
        val subject = decodeJwtPayloadSubject(oldAccessToken)
        userTokenRepository.find("${UserRefreshToken.prefix}${subject.split(':')[0]}") ?: throw ExpiredJwtException(null, null, "Refresh token is expired.")
        return generateAccessToken(subject)
    }

    @Transactional
    fun validateRefreshToken(refreshToken: String, oldAccessToken: String) {
        validateAndParseToken(refreshToken)
        val userId = decodeJwtPayloadSubject(oldAccessToken).split(':')[0]
        userTokenRepository.find("${UserRefreshToken.prefix}$userId")
            ?.takeIf { it == refreshToken } ?: throw ExpiredJwtException(null, null, "Refresh token is expired.")
    }

    fun getAccessTokenExpireHour(): Long {
        return jwtConfig.expirationHour.toLong()
    }

    fun getRefreshTokenExpireHour(): Long {
        return jwtConfig.refreshExpirationHour.toLong()
    }

    private fun validateAndParseToken(token: String?): Jws<Claims> {
        return Jwts.parserBuilder()	// validateTokenAndGetSubject()에서 따로 분리
            .setSigningKey(jwtConfig.secret.toByteArray())
            .build()
            .parseClaimsJws(token)!!
    }

    private fun decodeJwtPayloadSubject(oldAccessToken: String): String {
        return objectMapper.readValue(
            Base64.getUrlDecoder().decode(oldAccessToken.split('.')[1]).decodeToString(),
            Map::class.java,
        )["sub"].toString()
    }
}
