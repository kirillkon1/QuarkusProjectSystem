package ru.itmo.kafka

import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.slf4j.LoggerFactory
import ru.itmo.apiobjects.kafka.model.TaskRequest
import ru.itmo.apiobjects.kafka.model.TaskResponse
import ru.itmo.service.TaskService

@ApplicationScoped
class TaskRequestListener(private val taskService: TaskService, private val taskResponseService: TaskResponseService) {
    private val logger = LoggerFactory.getLogger(TaskRequestListener::class.java)

    @Incoming("task-requests")
    fun listen(request: TaskRequest): Uni<Void> {
        logger.info("Received Task Request: $request")
        return taskService.getTaskById(request.taskId)
            .onItem().ifNull().continueWith { null } // если задача не найдена, продолжим с null
            .flatMap { task ->
                if (task == null) {
                    // Отправляем пустой ответ, если задача не найдена
                    taskResponseService.sendTaskResponse(
                        TaskResponse(correlationId = request.correlationId),
                    )
                    logger.info("Sent Empty Task Response!")
                    Uni.createFrom().voidItem()
                } else {
                    // Создаем ответ на основе полученной задачи
                    val response =
                        TaskResponse(
                            correlationId = request.correlationId,
                            taskId = task.id,
                            title = task.title,
                            description = task.description ?: "",
                            dueDate = task.dueDate.toString(),
                            assignedTo = task.assignedTo,
                            projectId = task.projectId,
                            priority = task.priority.toString(),
                            status = task.status.toString(),
                            createdAt = task.createdAt.toString(),
                            updatedAt = task.updatedAt.toString(),
                        )
                    taskResponseService.sendTaskResponse(response)
                    logger.info("Sent Task Response: $response")
                    Uni.createFrom().voidItem()
                }
            }
    }
}
