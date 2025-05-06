package com.moneyforward.comp.webflux.controller

import com.moneyforward.comp.shared.data.GameMetricQuery
import com.moneyforward.comp.shared.data.GameResponse
import com.moneyforward.comp.webflux.peristence.Page
import com.moneyforward.comp.webflux.peristence.model.GameMetricModel
import com.moneyforward.comp.webflux.service.GameMetricService
import com.moneyforward.comp.webflux.service.GameService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/games")
class GameController(
    private val gameService: GameService,
    private val gameMetricService: GameMetricService
) {
    @GetMapping("/latest")
    fun latestGames(
        @RequestParam(required = false)
        count: Int = 3,
        @RequestHeader("Api-Host")
        host: String? = null
    ): Flux<GameResponse> {
        return gameService.getLatestGames(count, host)
    }

    @GetMapping("/metrics")
    fun metrics(
        @RequestParam(required = false) page: Long = 0,
        @RequestParam(required = false) name: String? = null,
        @RequestParam(required = false) developer: String? = null,
    ): Page<GameMetricModel> {
        return gameMetricService.getMetrics(
            GameMetricQuery(
                page = page,
                name = name,
                developer = developer
            )
        )
    }
}
