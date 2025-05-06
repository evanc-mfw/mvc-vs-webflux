package com.moneyforward.comp.mvc.persistence.repository

import com.moneyforward.comp.mvc.persistence.entity.GameMetricEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameMetricRepository : JpaRepository<GameMetricEntity, Long> {
    fun findByNameAndDeveloper(name: String, developer: String?): GameMetricEntity?
}