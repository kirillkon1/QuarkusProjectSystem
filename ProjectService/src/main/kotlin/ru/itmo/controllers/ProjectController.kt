@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.controllers

import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import ru.itmo.model.entity.ProjectEntity
import ru.itmo.service.ProjectService

@Path("api/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ProjectController
    @Inject
    constructor(private val projectService: ProjectService) {
        @GET
        fun getAllProjects(): Uni<Response> =
            projectService.getAllProjects()
                .onItem().transform { projects ->
                    Response.ok(projects).build()
                }

        @GET
        @Path("/{id}")
        fun getProjectById(
            @PathParam("id") id: Long,
        ): Uni<Response> =
            projectService.getProjectById(id)
                .onItem().transform { project ->
                    if (project != null) {
                        Response.ok(project).build()
                    } else {
                        Response.status(Response.Status.NOT_FOUND)
                            .entity("Сущность с id $id не найден")
                            .build()
                    }
                }

        @POST
        fun createProject(project: ProjectEntity): Uni<Response> =
            projectService.createProject(project)
                .onItem().transform { createdProject ->
                    Response.status(Response.Status.CREATED)
                        .entity(createdProject)
                        .build()
                }

        @PUT
        @Path("/{id}")
        fun updateProject(
            @PathParam("id") id: Long,
            project: ProjectEntity,
        ): Uni<Response> =
            projectService.updateProject(id, project)
                .onItem().transform { updatedProject ->
                    if (updatedProject != null) {
                        Response.ok(updatedProject).build()
                    } else {
                        Response.status(Response.Status.NOT_FOUND)
                            .entity("Сущность с id $id не найден")
                            .build()
                    }
                }

        @DELETE
        @Path("/{id}")
        fun deleteProject(
            @PathParam("id") id: Long,
        ): Uni<Response> =
            projectService.deleteProject(id)
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
