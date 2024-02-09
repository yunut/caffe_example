package com.assignment.caffe.adapter.`in`.web.security.jwt

import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider,
) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val token = parseBearerToken(request, HttpHeaders.AUTHORIZATION)
            val user = parseUserSpecification(token)
            UsernamePasswordAuthenticationToken.authenticated(user, token, user.authorities)
                .apply { details = WebAuthenticationDetails(request) }
                .also { SecurityContextHolder.getContext().authentication = it }
        } catch (e: ExpiredJwtException) {	// 변경
            reissueAccessToken(request, response, e)
        } catch (e: Exception) {
            request.setAttribute("exception", e)
        }

        filterChain.doFilter(request, response)
    }

    private fun parseBearerToken(request: HttpServletRequest, headerName: String): String? {
        return request.getHeader(headerName)
            .takeIf { it?.startsWith("Bearer ", true) ?: false }?.substring(7)
    }

    private fun parseUserSpecification(token: String?): User {
        return (
            token?.takeIf { it.length >= 10 }
                ?.let { jwtProvider.validateTokenAndGetSubject(it) }
                ?: "anonymous:anonymous"
            ).split(":")
            .let { User(it[0], "", listOf(SimpleGrantedAuthority(it[1]))) }
    }

    private fun reissueAccessToken(request: HttpServletRequest, response: HttpServletResponse, exception: Exception) {
        try {
            val refreshToken = parseBearerToken(request, "Refresh-Token") ?: throw exception
            val oldAccessToken = parseBearerToken(request, HttpHeaders.AUTHORIZATION)!!
            jwtProvider.validateRefreshToken(refreshToken, oldAccessToken)
            val newAccessToken = jwtProvider.reGenerateAccessToken(oldAccessToken)
            val user = parseUserSpecification(newAccessToken)
            UsernamePasswordAuthenticationToken.authenticated(user, newAccessToken, user.authorities)
                .apply { details = WebAuthenticationDetails(request) }
                .also { SecurityContextHolder.getContext().authentication = it }

            response.setHeader("New-Access-Token", newAccessToken)
        } catch (e: Exception) {
            request.setAttribute("exception", e)
        }
    }
}
