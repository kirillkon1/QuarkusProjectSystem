package ru.itmo.controllers

import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.config.inject.ConfigProperty
import ru.itmo.model.dto.request.CreateUserDto
import ru.itmo.model.dto.request.LoginRequestDto
import ru.itmo.model.dto.response.AuthResponseDto
import ru.itmo.service.AuthService
import java.time.LocalDateTime


@Path("api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AuthController @Inject constructor(private val authService: AuthService) {

    @POST
    @Path("/register")
    fun registerUser(request: CreateUserDto): Uni<Response> =
        authService.registerUser(request)
            .onItem().transform { user ->
                Response.status(Response.Status.CREATED)
                    .entity(user)
                    .build()
            }
            .onFailure(IllegalArgumentException::class.java).recoverWithItem { ex: Throwable ->
                Response.status(Response.Status.CONFLICT)
                    .entity(mapOf(
                        "error" to ex.message,
                        "time" to LocalDateTime.now(),
                        "status" to Response.Status.CONFLICT.statusCode
                    ))
                    .build()
            }

    @POST
    @Path("/login")
    fun login(request: LoginRequestDto): Uni<Response> =
        authService.authenticateUser(request.username, request.password)
            .onItem().transform { token ->
                if (token != null) {
                    Response.ok(AuthResponseDto(request.username, token)).build()
                } else {
                    Response.status(Response.Status.UNAUTHORIZED)
                        .entity(mapOf("error" to "Неверные учетные данные"))
                        .build()
                }
            }
}