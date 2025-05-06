package com.moneyforward.comp.webflux.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
    @Bean
    fun securityFilter(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.authorizeExchange { it.anyExchange().permitAll() }.build()
    }
}