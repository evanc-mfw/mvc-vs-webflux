package com.moneyforward.comp.webflux.peristence

data class Page<T>(val content: List<T>, val page: PageMeta) {
    constructor(content: List<T>, size: Long, number: Long, totalRecords: Long) : this(
        content,
        PageMeta(
            size,
            number,
            totalRecords,
            Math.ceilDiv(totalRecords, size)
        )
    )

    data class PageMeta(
        val size: Long,
        val number: Long,
        val totalElements: Long,
        val totalPages: Long
    )
}