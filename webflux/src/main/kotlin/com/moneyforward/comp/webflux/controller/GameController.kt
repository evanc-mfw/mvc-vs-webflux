package com.moneyforward.comp.webflux.controller

import com.moneyforward.comp.shared.data.GameMetricQuery
import com.moneyforward.comp.shared.data.GameResponse
import com.moneyforward.comp.webflux.peristence.Page
import com.moneyforward.comp.webflux.peristence.model.GameMetricModel
import com.moneyforward.comp.webflux.service.GameMetricService
import com.moneyforward.comp.webflux.service.GameService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

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
    ): Mono<ResponseEntity<List<GameResponse>>> {
        val games = gameService.getLatestGames(count, host)
        return games.map {
            if (it.isEmpty()) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(emptyList())
            } else {
                ResponseEntity.status(HttpStatus.OK).body(it)
            }
        }
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
