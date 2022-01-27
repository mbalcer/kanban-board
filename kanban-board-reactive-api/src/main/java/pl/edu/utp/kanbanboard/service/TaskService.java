package pl.edu.utp.kanbanboard.service;

import pl.edu.utp.kanbanboard.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {
    Flux<Task> all();

    Mono<Task> get(String id);

    Flux<Task> allByProjectId(String projectId);

    Mono<Task> create(Task newTask);

    Mono<Task> update(String id, Task updateTask);

    Mono<Task> delete(String id);
}
