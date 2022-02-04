package pl.edu.utp.kanbanboard.service;

import pl.edu.utp.kanbanboard.model.Project;
import pl.edu.utp.kanbanboard.model.RegisterEntry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface RegisterService {
    Flux<RegisterEntry> all();

    Flux<RegisterEntry> allByProjectId(String projectId);

    Flux<RegisterEntry> allByDate(LocalDate date);

    Mono<RegisterEntry> get(String id);

    Mono<RegisterEntry> getByProjectIdAndDate(String projectId, LocalDate date);

    Mono<Project> update(String projectId);

    // TODO:
    // Flux<RegisterEntry> updateAll();

}
