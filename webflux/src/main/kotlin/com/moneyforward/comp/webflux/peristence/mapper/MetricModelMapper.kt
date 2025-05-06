package com.moneyforward.comp.webflux.peristence.mapper

import com.moneyforward.comp.webflux.peristence.model.MetricModel
import com.moneyforward.comp.webflux.service.MetricService

object MetricModelMapper {
    fun toModel(stopwatch: MetricService.MetricStopwatch, traceId: String): MetricModel {
        return MetricModel(
            stopwatch.endpoint,
            stopwatch.elapsedTime,
            traceId
        )
    }
}