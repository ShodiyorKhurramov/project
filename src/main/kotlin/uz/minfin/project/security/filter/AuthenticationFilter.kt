package uz.minfin.project.security.filter

import com.auth0.jwt.JWT
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import uz.minfin.project.AppErrorDto
import uz.minfin.project.LoginDto
import uz.minfin.project.SessionDTO
import uz.minfin.project.security.Utils.JWTUtils
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse



class AuthenticationFilter(
) : UsernamePasswordAuthenticationFilter(
) {
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication {
        val loginDto = ObjectMapper().readValue(request.reader, LoginDto::class.java)
        val authenticationToken =
            UsernamePasswordAuthenticationToken(loginDto.userName, loginDto.password)
        return authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val user: User = authResult!!.principal as User
        val expiryForAccessToken = JWTUtils().getExpiry()
        val authorities = user.authorities.map { T -> T.authority }
        val accessToken = JWT.create()
            .withSubject(user.username)
            .withExpiresAt(expiryForAccessToken)
            .withIssuer(request!!.requestURL.toString())
            .withClaim("roles", authorities)
            .sign(JWTUtils().getAlgorithm())
        val sessionDTO = SessionDTO(
            expiryForAccessToken,
            System.currentTimeMillis(),
            accessToken
        )
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        ObjectMapper().writeValue(response.outputStream, sessionDTO)
    }


    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        ObjectMapper().writeValue(
            response.outputStream,
            AppErrorDto(
                null,
                HttpStatus.INTERNAL_SERVER_ERROR,
                null,
                failed.message,
                request.requestURI
            )
        )
    }

}