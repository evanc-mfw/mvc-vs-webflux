package com.moneyforward.comp.webflux.peristence.repository

import com.moneyforward.comp.webflux.peristence.model.AccessLogModel
import com.moneyforward.jooq.generated.tables.references.ACCESS_LOGS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class AccessLogRepository(private val dslContext: DSLContext) {
    fun insertAccessLog(accessLog: AccessLogModel) {
        dslContext.insertInto(ACCESS_LOGS)
            .set(ACCESS_LOGS.ENDPOINT, accessLog.endpoint)
            .set(ACCESS_LOGS.REQUEST_BODY, accessLog.request)
            .set(ACCESS_LOGS.RESPONSE_BODY, accessLog.response)
            .set(ACCESS_LOGS.HTTP_STATUS_CODE, accessLog.statusCode)
            .set(ACCESS_LOGS.CREATED_AT, accessLog.createdAt)
            .set(ACCESS_LOGS.UPDATED_AT, accessLog.updatedAt)
            .execute()
    }
}