package ru.itmo.utils

import org.mindrot.jbcrypt.BCrypt

object Hasher {

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return try {
            BCrypt.checkpw(password, hashedPassword) // Проверяем, что пароль хэширован корректно
        } catch (e: IllegalArgumentException) {
            println("[verifyPassword]")
            e.printStackTrace()
            false // Если ошибка, значит хэш некорректный → проверка не пройдена
        }
    }

}