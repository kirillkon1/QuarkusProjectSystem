package ru.itmo.service

import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.core.Vertx
import jakarta.enterprise.context.ApplicationScoped
import ru.itmo.model.entity.AnalyticItemEntity
import ru.itmo.model.repository.AnalyticsRepository

@ApplicationScoped
class AnalyticsService(
    private val analyticsRepository: AnalyticsRepository,
    private val vertx: Vertx,
) {
    @WithSession
    fun getAnalyticItemById(id: Long): Uni<AnalyticItemEntity?> = analyticsRepository.findById(id)

    @WithSession
    fun getAllAnalyticItems(): Uni<List<AnalyticItemEntity>> = analyticsRepository.listAll()

    @WithTransaction
    fun createByTaskResponse(item: AnalyticItemEntity): Uni<AnalyticItemEntity> {
        return analyticsRepository.persist(item)
    }
}
