package pl.edu.utp.kanbanboard.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.utp.kanbanboard.model.Task;
import reactor.core.publisher.Flux;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {
    Flux<Task> findByName(String name);
}
