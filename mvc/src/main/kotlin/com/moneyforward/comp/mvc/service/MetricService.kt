package com.moneyforward.comp.mvc.service

import com.moneyforward.comp.mvc.persistence.entity.MetricEntity
import com.moneyforward.comp.mvc.persistence.repository.MetricRepository
import org.slf4j.MDC
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class MetricService(private val metricRepository: MetricRepository) {
    fun startMetric(endpoint: String) = MetricStopwatch(endpoint, Instant.now())

    fun record(metric: MetricStopwatch) {
        metricRepository.save(MetricEntity(
            MDC.getCopyOfContextMap()["traceId"],
            metric.endpoint,
            metric.elapsedTime
        ))
    }

    data class MetricStopwatch(val endpoint: String, val start: Instant) {
        val elapsedTime get() = start.until(Instant.now(), ChronoUnit.MILLIS)
    }
}