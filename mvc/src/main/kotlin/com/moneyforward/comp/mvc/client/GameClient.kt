package com.moneyforward.comp.mvc.client

import com.moneyforward.comp.mvc.service.MetricService
import com.moneyforward.comp.shared.data.GameResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import kotlin.random.Random

@Component
class GameClient(
    private val metricService: MetricService,
    private val restTemplate: RestTemplate,
    @Value("\${hosts.games}")
    private val gameHost: String,
) {
    private val gameEndpoint = "/switch/games"

    fun getLatestGame(maxId: Int?, host: String?) : GameResponse? {
        val staticPath = "${host ?: gameHost}$gameEndpoint"
        val request = RequestEntity.get(
            "$staticPath/${Random.nextInt(maxId ?: 1000)}"
        ).build()
        val metric = metricService.startMetric(staticPath)

        try {
            return restTemplate.exchange(request, GameResponse::class.java).body!!
        } catch (e: HttpClientErrorException) {
            if (e.statusCode == HttpStatus.NOT_FOUND) return null
            throw e
        } finally {
            metricService.record(metric)
        }
    }
}