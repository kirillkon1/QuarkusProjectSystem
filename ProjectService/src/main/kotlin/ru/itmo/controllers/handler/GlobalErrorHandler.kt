@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.controllers.handler

import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import org.hibernate.exception.ConstraintViolationException
import java.util.*

@Provider
class GlobalErrorHandler : ExceptionMapper<Throwable> {
    override fun toResponse(exception: Throwable): Response {
        val errorResponse =
            mapOf(
                "timestamp" to Date(),
                "status" to
                    when (exception) {
                        is ConstraintViolationException -> Response.Status.CONFLICT.statusCode
                        is IllegalArgumentException -> Response.Status.BAD_REQUEST.statusCode
                        else -> Response.Status.INTERNAL_SERVER_ERROR.statusCode
                    },
                "message" to (exception.message ?: "Произошла ошибка"),
            )

        val status =
            when (exception) {
                is ConstraintViolationException -> Response.Status.CONFLICT
                is IllegalArgumentException -> Response.Status.BAD_REQUEST
                else -> Response.Status.INTERNAL_SERVER_ERROR
            }

        println(exception.printStackTrace())

        return Response.status(status).entity(errorResponse).build()
    }
}
