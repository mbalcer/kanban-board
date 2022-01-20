package pl.edu.utp.kanbanboard.service;

import reactor.core.publisher.Mono;

public interface RelationshipService {
    Mono<Boolean> isExistProject(String projectId);

    Mono<Boolean> isExistTask(String taskId);

    Mono<Boolean> isExistStudent(String studentId);
}
