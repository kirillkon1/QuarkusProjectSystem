package ru.itmo.apiobjects.kafka.model

data class TaskResponse(
    val correlationId: String,
    val taskId: Long? = null,
    val title: String? = null,
    val description: String? = null,
    val dueDate: String? = null,
    val assignedTo: Long? = null,
    val projectId: Long? = null,
    val priority: String? = null,
    val status: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)
