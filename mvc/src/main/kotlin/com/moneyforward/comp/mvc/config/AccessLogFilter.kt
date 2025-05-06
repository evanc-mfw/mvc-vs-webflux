package com.moneyforward.comp.mvc.config

import com.moneyforward.comp.mvc.persistence.entity.AccessLogEntity
import com.moneyforward.comp.mvc.persistence.repository.AccessLogRepository
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.ErrorResponse
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class AccessLogFilter(
    private val accessLogRepository: AccessLogRepository
) : Filter {
    private val logger: Logger = LoggerFactory.getLogger(AccessLogFilter::class.java)

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val cacheRequest = ContentCachingRequestWrapper(request as HttpServletRequest)
        val cacheResponse = ContentCachingResponseWrapper(response as HttpServletResponse)

        cacheRequest.getHeader("Api-Host")?.also {
            DebugHostContext.host = it
        }

        try {
            chain?.doFilter(cacheRequest, cacheResponse)
            createAccessLog(cacheRequest, cacheResponse)
        } catch (ex: Exception) {
            createAccessLog(cacheRequest, cacheResponse, ex as? ServletException)
            throw ex
        } finally {
            DebugHostContext.threadLocal.remove()
        }
    }

    private fun createAccessLog(
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
        ex: ServletException? = null
    ) {
        try {
            accessLogRepository.save(AccessLogEntity(
                request.requestURI,
                request.contentAsByteArray,
                response.contentAsByteArray,
                (ex as? ErrorResponse)?.statusCode?.value() ?: response.status
            ))
        } catch (ex: Exception) {
            logger.error("Critical exception while creating access log", ex)
        } finally {
            response.copyBodyToResponse()
        }
    }
}