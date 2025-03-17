package ru.itmo.model.dto.request

data class CreateUserDto(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
)
