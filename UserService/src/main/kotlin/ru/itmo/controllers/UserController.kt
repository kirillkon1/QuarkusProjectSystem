@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.controllers

import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import ru.itmo.model.entity.UserEntity
import ru.itmo.service.UserService

@Path("api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class UserController
    @Inject
    constructor(private val userService: UserService) {
        @GET
        fun getAllUsers(): Uni<Response> =
            userService.getAllUsers()
                .onItem().transform { users ->
                    Response.ok(users).build()
                }

        @GET
        @Path("/{id}")
        fun getUserById(
            @PathParam("id") id: Long,
        ): Uni<Response> =
            userService.getUserById(id)
                .onItem().transform { user ->
                    if (user != null) {
                        Response.ok(user).build()
                    } else {
                        Response.status(Response.Status.NOT_FOUND)
                            .entity("Сущность с id $id не найден")
                            .build()
                    }
                }

        @POST
        fun createUser(user: UserEntity): Uni<Response> =
            userService.createUser(user)
                .onItem().transform { createdUser ->
                    Response.status(Response.Status.CREATED)
                        .entity(createdUser)
                        .build()
                }

        @PUT
        @Path("/{id}")
        fun updateUser(
            @PathParam("id") id: Long,
            user: UserEntity,
        ): Uni<Response> =
            userService.updateUser(id, user)
                .onItem().transform { updatedUser ->
                    if (updatedUser != null) {
                        Response.ok(updatedUser).build()
                    } else {
                        Response.status(Response.Status.NOT_FOUND)
                            .entity("Сущность с id $id не найден")
                            .build()
                    }
                }

        @DELETE
        @Path("/{id}")
        fun deleteUser(
            @PathParam("id") id: Long,
        ): Uni<Response> =
            userService.deleteUser(id)
                .onItem().transform { deleted ->
                    if (deleted) {
                        Response.noContent().build()
                    } else {
                        Response.status(Response.Status.NOT_FOUND)
                            .entity("Сущность с id $id не найден")
                            .build()
                    }
                }
    }
