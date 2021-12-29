package pl.edu.utp.kanbanboard.service;

import pl.edu.utp.kanbanboard.model.Project;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectService {
    Flux<Project> all();

    Mono<Project> get(String id);

    Mono<Project> create(Project newProject);

    Mono<Project> update(String id, Project updateProject);

    Mono<Project> delete(String id);
}
