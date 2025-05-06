package com.moneyforward.comp.shared.data

data class GameResponse(
    val id: Long,
    val name: String,
    val genre: List<String>?,
    val developers: List<String>?,
    val publishers: List<String>?,
    val releaseDates: Map<String, String>?
)