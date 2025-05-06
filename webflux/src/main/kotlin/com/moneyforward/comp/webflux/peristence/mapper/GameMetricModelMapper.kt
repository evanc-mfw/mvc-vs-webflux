package com.moneyforward.comp.webflux.peristence.mapper

import com.moneyforward.comp.shared.data.GameResponse
import com.moneyforward.comp.webflux.peristence.model.GameMetricModel

object GameMetricModelMapper {
    fun toModel(gameResponse: GameResponse): GameMetricModel {
        return GameMetricModel(
            name = gameResponse.name,
            developer = gameResponse.developers?.getOrNull(0)
        )
    }
}