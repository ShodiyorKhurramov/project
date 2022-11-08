package uz.minfin.project.security.filter

import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import uz.minfin.project.security.Utils.JWTUtils
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
class AuthenticationJwtTokenFilter(
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("Authorization")
        if (token != null && token.startsWith("Bearer ")) {
            val girttoken = token.substring(7)
            val decodedJWT = JWTUtils().getVerifier()!!.verify(girttoken)
            val username = decodedJWT.subject
            val roles = decodedJWT.getClaim("roles").asArray(String::class.java)

            val authorities: MutableCollection<SimpleGrantedAuthority> = ArrayList()
            Arrays.stream(roles).forEach { role: String? ->
                authorities.add(
                    SimpleGrantedAuthority(role)
                )
            }
            val authenticationToken = UsernamePasswordAuthenticationToken(username, null, authorities)
            SecurityContextHolder.getContext().authentication = authenticationToken
        }
        filterChain.doFilter(request, response)
    }
}