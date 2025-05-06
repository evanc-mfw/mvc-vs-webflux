package com.moneyforward.comp.mvc.persistence.repository

import com.moneyforward.comp.mvc.persistence.entity.AccessLogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccessLogRepository : JpaRepository<AccessLogEntity, Long>