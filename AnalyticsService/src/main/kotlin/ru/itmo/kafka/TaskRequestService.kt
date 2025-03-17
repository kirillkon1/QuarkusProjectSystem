package ru.itmo.kafka

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import ru.itmo.apiobjects.kafka.model.TaskRequest

@ApplicationScoped
class TaskRequestService(
    @Channel("task-requests") private val emitter: Emitter<TaskRequest>,
) {
    fun sendTaskResponse(taskId: Long) {
        val req =
            TaskRequest(
                taskId = taskId,
                correlationId = (Math.random() * 100000).toLong().toString(),
            )

        emitter.send(req)
    }
}
