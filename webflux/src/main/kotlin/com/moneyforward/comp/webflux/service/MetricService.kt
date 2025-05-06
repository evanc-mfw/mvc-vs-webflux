package com.moneyforward.comp.webflux.service

import com.moneyforward.comp.webflux.peristence.mapper.MetricModelMapper
import com.moneyforward.comp.webflux.peristence.repository.MetricRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class MetricService(private val metricRepository: MetricRepository) {
    fun startMetric(endpoint: String) = MetricStopwatch(endpoint, Instant.now())

    fun record(metric: MetricStopwatch) {
        metricRepository.insertMetric(MetricModelMapper.toModel(metric, ""))
    }

    data class MetricStopwatch(val endpoint: String, val start: Instant) {
        val elapsedTime get() = start.until(Instant.now(), ChronoUnit.MILLIS)
    }
}