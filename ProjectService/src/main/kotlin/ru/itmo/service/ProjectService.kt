package ru.itmo.service

import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import ru.itmo.model.entity.ProjectEntity
import ru.itmo.model.repository.ProjectRepository

@ApplicationScoped
class ProjectService(private val projectRepository: ProjectRepository) {
    @WithSession
    fun getProjectById(id: Long): Uni<ProjectEntity?> = projectRepository.findById(id)

    @WithSession
    fun getAllProjects(): Uni<List<ProjectEntity>> = projectRepository.listAll()

    @WithTransaction
    fun createProject(project: ProjectEntity): Uni<ProjectEntity> {
        project.budget = Math.random() * 10000
        project.location = "Location is #${(Math.random() * 1000).toInt()}"

        return projectRepository.persist(project)
    }

    @WithTransaction
    fun updateProject(
        id: Long,
        updatedProject: ProjectEntity,
    ): Uni<ProjectEntity?> =
        projectRepository.findById(id)
            .onItem().ifNotNull().transformToUni { existingProject ->
                existingProject.name = updatedProject.name
                existingProject.description = updatedProject.description

                existingProject.budget = Math.random() * 10000
                existingProject.location = "Location is #${(Math.random() * 1000).toInt()}"

                projectRepository.persist(existingProject)
            }

    @WithTransaction
    fun deleteProject(id: Long): Uni<Boolean> = projectRepository.deleteById(id)
}
