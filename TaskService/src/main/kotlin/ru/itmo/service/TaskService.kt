package ru.itmo.service

import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import ru.itmo.model.entity.TaskEntity
import ru.itmo.model.repository.TaskRepository

@ApplicationScoped
class TaskService(private val taskRepository: TaskRepository) {
    @WithSession
    fun getTaskById(id: Long): Uni<TaskEntity?> = taskRepository.findById(id)

    @WithSession
    fun getAllTasks(): Uni<List<TaskEntity>> = taskRepository.listAll()

    @WithTransaction
    fun createTask(task: TaskEntity): Uni<TaskEntity> {
        return taskRepository.persist(task)
    }

    @WithTransaction
    fun updateTask(
        id: Long,
        updatedTask: TaskEntity,
    ): Uni<TaskEntity?> =
        taskRepository.findById(id)
            .onItem().ifNotNull().transformToUni { existingTask ->
                existingTask.title = updatedTask.title
                existingTask.description = updatedTask.description
                taskRepository.persist(existingTask)
            }

    @WithTransaction
    fun deleteTask(id: Long): Uni<Boolean> = taskRepository.deleteById(id)
}
