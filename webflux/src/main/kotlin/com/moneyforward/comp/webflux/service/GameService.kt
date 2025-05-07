package com.moneyforward.comp.webflux.service

import com.moneyforward.comp.shared.data.GameResponse
import com.moneyforward.comp.webflux.client.GameClient
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class GameService(
    private val gameClient: GameClient,
    private val gameMetricService: GameMetricService
) {
    fun getLatestGames(count: Int, host: String?): Mono<List<GameResponse>> {
        val gameStream = Flux.merge(
            (0..<count).map { gameClient.getLatestGame(host) }
        )
        return gameStream
            .onErrorResume { err ->
                when {
                    err !is WebClientResponseException -> Mono.error(err)
                    err.statusCode == HttpStatus.NOT_FOUND -> Mono.empty()
                    else -> Mono.error(err)
                }
            }
            .collectList()
            .doOnNext {
                gameMetricService.updateOrCreateMetrics(it)
            }
    }
}
