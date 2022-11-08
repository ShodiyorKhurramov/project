package uz.minfin.project.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import uz.minfin.project.AuthService
import uz.minfin.project.security.filter.AuthenticationFilter
import uz.minfin.project.security.filter.AuthenticationJwtTokenFilter


@Configuration
class SecurityConfiguration(
    private val authService: AuthService
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {

        http
            .headers().defaultsDisabled().cacheControl()
        http.cors().and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
            .antMatchers("/**")
            .permitAll()
            .anyRequest().authenticated()
        http.addFilterBefore(AuthenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
        http.addFilter(AuthenticationFilter())

    }


    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    @Bean
    fun encoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }


}