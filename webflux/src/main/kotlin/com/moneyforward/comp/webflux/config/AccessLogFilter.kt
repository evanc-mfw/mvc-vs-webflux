package com.moneyforward.comp.webflux.config

import com.moneyforward.comp.webflux.peristence.model.AccessLogModel
import com.moneyforward.comp.webflux.peristence.repository.AccessLogRepository
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class AccessLogFilter(
    private val accessLogRepository: AccessLogRepository
) : WebFilter {
    private val logger: Logger = LoggerFactory.getLogger(AccessLogFilter::class.java)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val cachingRequestDecorator = CachingServerHttpRequest(exchange.request)
        val cachingResponseDecorator = CachingServerHttpResponse(exchange.response)

        val mutatedExchange = exchange.mutate()
            .request(cachingRequestDecorator)
            .response(cachingResponseDecorator)
            .build()

        return chain.filter(mutatedExchange).doOnTerminate {
            createAccessLog(cachingRequestDecorator, cachingResponseDecorator)
        }
    }

    private fun createAccessLog(
        request: CachingServerHttpRequest,
        response: CachingServerHttpResponse
    ) {
        try {
            accessLogRepository.insertAccessLog(AccessLogModel(
                request.uri.toString(),
                request.buffer.toByteArray(),
                response.buffer.toByteArray(),
                response.statusCode?.value() ?: 200
            ))
        } catch (ex: Exception) {
            logger.error("Critical exception while creating access log", ex)
        }
    }

    private class CachingServerHttpRequest(request: ServerHttpRequest) : ServerHttpRequestDecorator(request) {
        val buffer = mutableListOf<Byte>()

        private val body = request.body.doOnNext { dataBuffer ->
            buffer.addAll(dataBuffer.asByteBuffer().array().asIterable())
        }

        override fun getBody(): Flux<DataBuffer> = body
    }

    private class CachingServerHttpResponse(delegate: ServerHttpResponse) : ServerHttpResponseDecorator(delegate) {
        val buffer = mutableListOf<Byte>()
        private val bufferFactory = delegate.bufferFactory()

        override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
            val fluxBody = Flux.from(body)
                .doOnNext { dataBuffer ->
                    buffer.addAll(dataBuffer.asInputStream().readAllBytes().asIterable())
                    dataBuffer.readPosition(0)
                }
                .map { dataBuffer ->
                    bufferFactory.wrap(dataBuffer.asByteBuffer())
                }
            return super.writeWith(fluxBody)
        }
    }
}