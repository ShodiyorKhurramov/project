package uz.minfin.project.security.Utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class JWTUtils {
    var expiry = 1800000000
    var secret = "SD784SRTED34JNBNJ@@*&p45kbNBKRIIHB@456#$%&f%&t#sxyASX345"

    fun getExpiry(): Date {
        return Date(System.currentTimeMillis() + expiry)
    }
    fun getExpiryForRefreshToken(): Date {
        return Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(40))
    }
    fun getAlgorithm(): Algorithm? {
        return Algorithm.HMAC256(secret.toByteArray())
    }
    fun getVerifier(): JWTVerifier? {
        return JWT.require(getAlgorithm()).build()
    }
    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder(4)
    }

}