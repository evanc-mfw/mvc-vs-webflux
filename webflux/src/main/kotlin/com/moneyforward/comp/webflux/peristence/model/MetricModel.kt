package com.moneyforward.comp.webflux.peristence.model

import java.time.LocalDateTime

data class MetricModel(
    val endpoint: String,
    val executionTime: Long,
    val traceId: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = createdAt,
    val id: Long = 0
)
