package com.moneyforward.comp.mvc.client

import com.moneyforward.comp.mvc.service.MetricService
import com.moneyforward.comp.shared.data.GameResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import kotlin.random.Random

@Component
class GameClient(
    private val metricService: MetricService,
    private val restTemplate: RestTemplate,
    @Value("\${hosts.games}")
    private val gameHost: String
) {
    private val gameEndpoint = "/switch/games"

    fun getLatestGame(host: String?) : GameResponse {
        val staticPath = "${host ?: gameHost}$gameEndpoint"
        val request = RequestEntity.get(
            "$staticPath/${Random.nextInt(1000)}"
        ).build()
        val metric = metricService.startMetric(staticPath)

        try {
            return restTemplate.exchange(request, GameResponse::class.java).body!!
        } finally {
            metricService.record(metric)
        }
    }
}