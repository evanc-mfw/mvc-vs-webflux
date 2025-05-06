package com.moneyforward.comp.mvc.service

import com.moneyforward.comp.mvc.persistence.entity.GameMetricEntity
import com.moneyforward.comp.mvc.persistence.repository.GameMetricRepository
import com.moneyforward.comp.shared.data.GameMetricQuery
import com.moneyforward.comp.shared.data.GameResponse
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class GameMetricService(private val repository: GameMetricRepository) {
    private val pageSize = 25

    fun updateOrCreateMetrics(games: List<GameResponse>) {
        games.distinctBy { it.name + it.developers?.getOrNull(0) }.forEach(this::updateOrCreateMetric)
    }

    fun updateOrCreateMetric(game: GameResponse) {
        val name = game.name
        val developer = game.developers?.getOrNull(0)
        val entry = repository.findByNameAndDeveloper(name, developer)
        val updateRecord = entry?.apply { latestCount += 1 }
            ?: GameMetricEntity(
                name = name,
                developer = developer,
                latestCount = 1
            )
        repository.save(updateRecord)
    }

    fun getMetrics(query: GameMetricQuery): Page<GameMetricEntity> {
        val pageRequest = PageRequest.of(query.page.toInt(), pageSize)

        var matcher = ExampleMatcher.matching()
            .withIgnorePaths("latestCount", "createdAt", "updatedAt", "id")
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)

        if (query.name == null) matcher = matcher.withIgnorePaths("name")
        matcher = matcher.withIgnoreCase().withIgnoreNullValues()

        val example = Example.of(GameMetricEntity(
            query.name ?: "",
            query.developer,
            0
        ), matcher)

        return repository.findAll(example, pageRequest)
    }
}