package pl.edu.utp.kanbanboard.service.impl;

import org.springframework.stereotype.Service;
import pl.edu.utp.kanbanboard.model.Project;
import pl.edu.utp.kanbanboard.repository.ProjectRepository;
import pl.edu.utp.kanbanboard.repository.TaskRepository;
import pl.edu.utp.kanbanboard.service.ProjectService;
import pl.edu.utp.kanbanboard.service.RelationshipService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final RelationshipService relationshipService;

    public ProjectServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository, RelationshipService relationshipService) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.relationshipService = relationshipService;
    }

    @Override
    public Flux<Project> all() {
        return this.projectRepository.findAll();
    }

    @Override
    public Mono<Project> get(String id) {
        return this.projectRepository.findById(id);
    }

    @Override
    public Mono<Project> create(Project newProject) {
        return Mono.just(newProject)
                .filterWhen(project -> {
                    if (!project.getStudentIds().isEmpty()) {
                        return Flux.fromIterable(project.getStudentIds())
                                .flatMap(relationshipService::isExistStudent)
                                .all(isExist -> isExist);
                    } else {
                        return Mono.just(true);
                    }
                })
                .filterWhen(project -> {
                    if (!project.getTaskIds().isEmpty()) {
                        return Flux.fromIterable(project.getTaskIds())
                                .flatMap(relationshipService::isExistTask)
                                .all(isExist -> isExist);
                    } else {
                        return Mono.just(true);
                    }
                })
                .map(project -> {
                    project.setProjectId(UUID.randomUUID().toString());
                    project.setCreateDateTime(LocalDateTime.now());
                    project.setUpdateDateTime(LocalDateTime.now());
                    return project;
                })
                .flatMap(projectRepository::save)
                .doOnSuccess(project -> {
                    if (project == null) {
                        throw new IllegalArgumentException("The task ids or student ids that you entered does not exist");
                    }
                });
    }

    @Override
    public Mono<Project> update(String id, Project updateProject) {
        return this.projectRepository
                .findById(id)
                .doOnNext(project -> {
                    project.setName(updateProject.getName());
                    project.setDescription(updateProject.getDescription());
                    project.setSubmitDateTime(updateProject.getSubmitDateTime());
                    project.setUpdateDateTime(LocalDateTime.now());
                })
                .flatMap(this.projectRepository::save);
    }

    @Override
    public Mono<Project> delete(String id) {
        return this.projectRepository
                .findById(id)
                .flatMap(s -> this.taskRepository.deleteByProjectId(id)
                        .then(this.projectRepository.deleteById(s.getProjectId()))
                        .thenReturn(s));
    }
}
