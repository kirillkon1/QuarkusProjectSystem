package ru.itmo.apiobjects.kafka.model

data class TaskRequest(
    val correlationId: String,
    val taskId: Long,
)
