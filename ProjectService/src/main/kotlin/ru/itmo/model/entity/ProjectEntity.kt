@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "projects")
class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    var name: String? = null

    var description: String? = null
    var location: String? = null
    var startDate: LocalDateTime? = null
    var endDate: LocalDateTime? = null
    var budget: Double? = null

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
