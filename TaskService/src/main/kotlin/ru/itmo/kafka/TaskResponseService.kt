package ru.itmo.kafka

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import ru.itmo.apiobjects.kafka.model.TaskResponse

@ApplicationScoped
class TaskResponseService(
    @Channel("task-responses") private val emitter: Emitter<TaskResponse>,
) {
    fun sendTaskResponse(response: TaskResponse) {
        emitter.send(response)
    }
}
