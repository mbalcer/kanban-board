package pl.edu.utp.kanbanboard.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.utp.kanbanboard.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {
    Flux<Task> findByName(String name);
    Flux<Task> findAllByProject(String projectId);
    Mono<Void> deleteByProject(String projectId);
}
