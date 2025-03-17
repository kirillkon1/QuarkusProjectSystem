@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "task_analytics")
class AnalyticItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(nullable = false)
    var title: String? = null

    var description: String? = null

    var status: String? = null

    var priority: String? = null

    var assignedTo: Long = 0

    var projectId: Long = 0

    var dueDate: LocalDateTime? = null

    open var createdAt: LocalDateTime? = null

    open var updatedAt: LocalDateTime? = null

    @PrePersist
    fun prePersist() {
        val now = LocalDateTime.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
