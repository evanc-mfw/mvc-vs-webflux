package com.moneyforward.comp.webflux.service

import com.moneyforward.comp.shared.data.GameResponse
import com.moneyforward.comp.webflux.client.GameClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class GameService(
    private val gameClient: GameClient,
    private val gameMetricService: GameMetricService
) {
    fun getLatestGames(count: Int): Flux<GameResponse> {
        val gameStream = Flux.merge(
            (0..<count).map { gameClient.getLatestGame(null) }
        )
        return gameStream
            .collectList()
            .doOnNext {
                gameMetricService.updateOrCreateMetrics(it)
            }
            // important: this flux is now an eager stream since we had to collect all sources into a Mono list
            .flatMapMany { Flux.fromIterable(it) }
    }
}
