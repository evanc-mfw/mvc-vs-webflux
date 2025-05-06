package com.moneyforward.comp.mvc.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {
    @Bean
    fun securityFilter(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests { it.anyRequest().permitAll() }.build()
    }
}