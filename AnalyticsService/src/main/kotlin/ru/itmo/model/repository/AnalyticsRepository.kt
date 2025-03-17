package ru.itmo.model.repository

import io.quarkus.hibernate.reactive.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import ru.itmo.model.entity.AnalyticItemEntity

@ApplicationScoped
class AnalyticsRepository : PanacheRepository<AnalyticItemEntity>
