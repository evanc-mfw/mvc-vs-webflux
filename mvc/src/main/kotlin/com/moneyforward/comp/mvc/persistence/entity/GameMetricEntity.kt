package com.moneyforward.comp.mvc.persistence.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity(name = "game_metrics")
class GameMetricEntity(
    val name: String,
    val developer: String?,
    var latestCount: Long,
    @CreationTimestamp @JsonIgnore
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @UpdateTimestamp @JsonIgnore
    val updatedAt: LocalDateTime = createdAt,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
)
