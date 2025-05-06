package com.moneyforward.comp.shared.data

data class GameMetricQuery(
    val page: Long,
    val name: String? = null,
    val developer: String? = null,
) {
    companion object {
        const val PAGE_SIZE = 25L
    }
}