package com.moneyforward.comp.webflux.client

import com.moneyforward.comp.shared.data.GameResponse
import com.moneyforward.comp.webflux.service.MetricService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import kotlin.random.Random

@Component
class GameClient(
    private val metricService: MetricService,
    private val webClient: WebClient,
    @Value("\${hosts.games}")
    private val gameHost: String
) {
    private val gameEndpoint = "/switch/games"

    fun getLatestGame(maxId: Int? = null, host: String?) : Mono<GameResponse> {
        val staticPath = "${host ?: gameHost}$gameEndpoint"
        val metric = metricService.startMetric(staticPath)

        try {
            return webClient.get()
                .uri("$staticPath/${Random.nextInt(maxId ?: 1000)}")
                .retrieve()
                .bodyToMono(GameResponse::class.java)
        } finally {
            metricService.record(metric)
        }
    }
}