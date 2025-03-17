package ru.itmo.kafka

import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.slf4j.LoggerFactory
import ru.itmo.apiobjects.kafka.model.TaskResponse
import ru.itmo.model.entity.AnalyticItemEntity
import ru.itmo.service.AnalyticsService
import java.time.LocalDateTime

@ApplicationScoped
class TaskResponseListener(private val service: AnalyticsService) {
    private val logger = LoggerFactory.getLogger(TaskResponseListener::class.java)

    @Incoming("task-responses")
    @WithTransaction
    fun listen(response: TaskResponse): Uni<Void> {
        logger.info("Received Task Response: $response")

        val item =
            AnalyticItemEntity().apply {
                title = (response.title ?: "default title") + " W/ KAFKA #${response.correlationId}"
                description = (response.description ?: "default description") +
                    "/w kafka #${(Math.random() * 1000000).toInt()}"
                assignedTo = response.assignedTo ?: 0
                dueDate = LocalDateTime.now()
                priority = response.priority.toString()
                projectId = response.projectId ?: -1
                status = response.status.toString()
            }

        return service.createByTaskResponse(item)
            .onItem().ignore().andContinueWithNull()
            .invoke { _: Void? -> logger.info("Analytic item persisted successfully") }
            .onFailure().invoke { e -> logger.error("Error persisting analytic item", e) }
    }
}
