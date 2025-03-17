package ru.itmo.service

import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.jwt.build.Jwt
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty
import ru.itmo.model.dto.request.CreateUserDto
import ru.itmo.model.entity.UserEntity
import ru.itmo.model.repository.UserRepository
import ru.itmo.utils.Hasher

@ApplicationScoped
class AuthService
    @Inject
    constructor(private val userRepository: UserRepository) {
        @ConfigProperty(name = "quarkus.jwt.token-lifetime", defaultValue = "3600")
        lateinit var jwtLivingDuration: String

        @ConfigProperty(name = "quarkus.jwt.issuer")
        lateinit var issuer: String

        @ConfigProperty(name = "quarkus.jwt.sign.key.secret")
        lateinit var secretKey: String

        @WithTransaction
        fun registerUser(request: CreateUserDto): Uni<UserEntity> {
            return userRepository.find("email", request.email).firstResult<UserEntity>()
                .onItem().ifNotNull().failWith {
                    IllegalArgumentException("Сущность с таким email уже существует")
                }
                .onItem().transformToUni { _: UserEntity ->
                    val user =
                        UserEntity().apply {
                            username = request.username
                            password = Hasher.hashPassword(request.password)
                            email = request.email
                        }
                    userRepository.persistAndFlush(user)
                }
        }

        @WithSession
        fun authenticateUser(
            username: String,
            password: String,
        ): Uni<String?> {
            return userRepository.find("username", username).firstResult<UserEntity>()
                .map { user ->
                    if (user != null && Hasher.verifyPassword(password, user.password)) {
                        generateToken(user.username)
                    } else {
                        null
                    }
                }
        }

        private fun generateToken(username: String): String {
            val _tokenDuration: Long = jwtLivingDuration.toLongOrNull() ?: 36000L

            return Jwt.issuer(issuer)
                .subject(username)
                .expiresIn(_tokenDuration)
                .sign(secretKey)
        }
    }
