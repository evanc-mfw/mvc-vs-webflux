package com.moneyforward.comp.webflux.peristence.model

import java.time.LocalDateTime

data class AccessLogModel(
    val endpoint: String,
    val request: ByteArray,
    val response: ByteArray,
    val statusCode: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = createdAt,
    val id: Long = 0
)