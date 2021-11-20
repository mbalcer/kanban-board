package pl.edu.utp.kanbanboard.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.utp.kanbanboard.model.Student;
import reactor.core.publisher.Mono;

public interface StudentRepository extends ReactiveMongoRepository<Student, String> {
    Mono<Student> getByEmail(String email);
}
