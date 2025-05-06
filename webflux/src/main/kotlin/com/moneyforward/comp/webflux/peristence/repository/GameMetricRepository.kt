package com.moneyforward.comp.webflux.peristence.repository

import com.moneyforward.comp.shared.data.GameMetricQuery
import com.moneyforward.comp.webflux.peristence.Page
import com.moneyforward.comp.webflux.peristence.model.GameMetricModel
import com.moneyforward.jooq.generated.tables.references.GAME_METRICS
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.SelectConditionStep
import org.jooq.SelectWhereStep
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class GameMetricRepository(private val dslContext: DSLContext) {
    fun upsertMetric(gameMetric: GameMetricModel) {
        val updateCount = dslContext.update(GAME_METRICS)
            .set(GAME_METRICS.LATEST_COUNT, GAME_METRICS.LATEST_COUNT.plus(1))
            .where(GAME_METRICS.NAME.eq(gameMetric.name))
            .and(GAME_METRICS.DEVELOPER.eq(gameMetric.developer))
            .execute()

        if (updateCount != 0) return

        dslContext.insertInto(GAME_METRICS)
            .set(GAME_METRICS.NAME, gameMetric.name)
            .set(GAME_METRICS.DEVELOPER, gameMetric.developer)
            .set(GAME_METRICS.LATEST_COUNT, 1)
            .set(GAME_METRICS.CREATED_AT, gameMetric.createdAt)
            .set(GAME_METRICS.UPDATED_AT, gameMetric.updatedAt)
            .execute()
    }

    fun search(query: GameMetricQuery): Page<GameMetricModel> {
        val queriedRecords = dslContext.selectFrom(GAME_METRICS)
            .filterByObject(query)
            .limit(GameMetricQuery.PAGE_SIZE)
            .offset(query.page * GameMetricQuery.PAGE_SIZE)
            .fetchInto(GameMetricModel::class.java)

        // we must make a second query to get the record count...
        val totalRecords = dslContext
            .selectCount()
            .from(GAME_METRICS)
            .filterByObject(query)
            .fetchOne(0, Long::class.java) ?: 0

        return Page(queriedRecords, GameMetricQuery.PAGE_SIZE, query.page, totalRecords)
    }

    private fun <T : Record?> SelectWhereStep<T>.filterByObject(query: GameMetricQuery): SelectConditionStep<T> {
        return where(DSL.trueCondition().let { cond ->
            if (query.name != null) {
                cond.and(GAME_METRICS.NAME.eq(query.name))
            } else cond
        }).and(DSL.trueCondition().let { cond ->
            if (query.developer != null) {
                cond.and(GAME_METRICS.DEVELOPER.eq(query.developer))
            } else cond
        })
    }
}