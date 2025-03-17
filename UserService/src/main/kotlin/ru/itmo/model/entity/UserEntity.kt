@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null
    lateinit var username: String

    @JsonIgnore
    lateinit var password: String

    @Column(unique = true)
    open lateinit var email: String

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
