package com.moneyforward.comp.webflux.peristence.repository

import com.moneyforward.comp.webflux.peristence.model.MetricModel
import com.moneyforward.jooq.generated.tables.references.METRICS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class MetricRepository(private val dslContext: DSLContext) {
    fun insertMetric(metric: MetricModel) {
        dslContext.insertInto(METRICS)
            .set(METRICS.ENDPOINT, metric.endpoint)
            .set(METRICS.EXECUTION_TIME, metric.executionTime)
            .set(METRICS.CREATED_AT, metric.createdAt)
            .set(METRICS.UPDATED_AT, metric.updatedAt)
            .execute()
    }
}