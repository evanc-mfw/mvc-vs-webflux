package com.moneyforward.comp.webflux.service

import com.moneyforward.comp.shared.data.GameMetricQuery
import com.moneyforward.comp.shared.data.GameResponse
import com.moneyforward.comp.webflux.peristence.Page
import com.moneyforward.comp.webflux.peristence.mapper.GameMetricModelMapper
import com.moneyforward.comp.webflux.peristence.model.GameMetricModel
import com.moneyforward.comp.webflux.peristence.repository.GameMetricRepository
import org.springframework.stereotype.Service

@Service
class GameMetricService(private val repository: GameMetricRepository) {
    fun updateOrCreateMetrics(games: List<GameResponse>) {
        games.distinctBy { it.name + it.developers?.getOrNull(0) }
            .map(GameMetricModelMapper::toModel)
            .forEach(repository::upsertMetric)
    }

    fun getMetrics(query: GameMetricQuery): Page<GameMetricModel> {
        return repository.search(query)
    }
}