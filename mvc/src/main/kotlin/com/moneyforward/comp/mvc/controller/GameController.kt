package com.moneyforward.comp.mvc.controller

import com.moneyforward.comp.mvc.persistence.entity.GameMetricEntity
import com.moneyforward.comp.mvc.service.GameMetricService
import com.moneyforward.comp.mvc.service.GameService
import com.moneyforward.comp.shared.data.GameMetricQuery
import com.moneyforward.comp.shared.data.GameResponse
import org.springframework.data.web.PagedModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/games")
class GameController(
    private val gameService: GameService,
    private val gameMetricService: GameMetricService
) {
    @GetMapping("/latest")
    fun latestGames(
        @RequestParam(required = false)
        count: Int = 3
    ): List<GameResponse> {
        return gameService.getLatestGames(count)
    }

    @GetMapping("/metrics")
    fun metrics(
        @RequestParam(required = false) page: Long = 0,
        @RequestParam(required = false) name: String? = null,
        @RequestParam(required = false) developer: String? = null,
    ): PagedModel<GameMetricEntity> {
        return PagedModel(gameMetricService.getMetrics(
            GameMetricQuery(
                page = page,
                name = name,
                developer = developer
            )
        ))
    }
}
