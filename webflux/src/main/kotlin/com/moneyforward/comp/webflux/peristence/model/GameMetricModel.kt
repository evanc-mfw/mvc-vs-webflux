package com.moneyforward.comp.webflux.peristence.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime

data class GameMetricModel(
    val name: String,
    val developer: String?,
    val latestCount: Long = 0,
    @JsonIgnore
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @JsonIgnore
    val updatedAt: LocalDateTime = createdAt,
    val id: Long = 0
)