@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.controllers

import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import ru.itmo.model.entity.TaskEntity
import ru.itmo.service.TaskService

@Path("api/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class TaskController
    @Inject
    constructor(private val taskService: TaskService) {
        @GET
        fun getAllTasks(): Uni<Response> =
            taskService.getAllTasks()
                .onItem().transform { tasks ->
                    Response.ok(tasks).build()
                }

        @GET
        @Path("/{id}")
        fun getTaskById(
            @PathParam("id") id: Long,
        ): Uni<Response> =
            taskService.getTaskById(id)
                .onItem().transform { task ->
                    if (task != null) {
                        Response.ok(task).build()
                    } else {
                        Response.status(Response.Status.NOT_FOUND)
                            .entity("Сущность с id $id не найден")
                            .build()
                    }
                }

        @POST
        fun createTask(task: TaskEntity): Uni<Response> =
            taskService.createTask(task)
                .onItem().transform { createdTask ->
                    Response.status(Response.Status.CREATED)
                        .entity(createdTask)
                        .build()
                }

        @PUT
        @Path("/{id}")
        fun updateTask(
            @PathParam("id") id: Long,
            task: TaskEntity,
        ): Uni<Response> =
            taskService.updateTask(id, task)
                .onItem().transform { updatedTask ->
                    if (updatedTask != null) {
                        Response.ok(updatedTask).build()
                    } else {
                        Response.status(Response.Status.NOT_FOUND)
                            .entity("Сущность с id $id не найден")
                            .build()
                    }
                }

        @DELETE
        @Path("/{id}")
        fun deleteTask(
            @PathParam("id") id: Long,
        ): Uni<Response> =
            taskService.deleteTask(id)
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
