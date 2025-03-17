@file:Suppress("ktlint:standard:filename", "ktlint:standard:no-wildcard-imports")

package ru.itmo.controllers

import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import ru.itmo.kafka.TaskRequestService
import ru.itmo.service.AnalyticsService

@Path("api/analytics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AnalyticController
    @Inject
    constructor(private val analyticsService: AnalyticsService, private val kafkaService: TaskRequestService) {
        @GET
        fun getAllAnalytics(): Uni<Response> =
            analyticsService.getAllAnalyticItems()
                .onItem().transform { items ->
                    Response.ok(items).build()
                }

        @GET
        @Path("/{id}")
        fun getAnalyticById(
            @PathParam("id") id: Long,
        ): Uni<Response> =
            analyticsService.getAnalyticItemById(id)
                .onItem().transform { items ->
                    if (items != null) {
                        Response.ok(items).build()
                    } else {
                        Response.status(Response.Status.NOT_FOUND)
                            .entity("Сущность с id $id не найден")
                            .build()
                    }
                }

        @POST
        @Path("/tasks/request")
        fun requestTask(
            @QueryParam("taskId") taskId: Long,
        ): Uni<Response> {
            // Отправка сообщения в Kafka и получение идентификатора корреляции.
            val correlationId = kafkaService.sendTaskResponse(taskId)
            return Uni.createFrom().item(
                Response.ok("Request sent with Correlation ID: $correlationId").build(),
            )
        }
    }
