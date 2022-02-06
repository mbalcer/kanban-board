package pl.edu.utp.kanbanboard.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.utp.kanbanboard.model.RegisterEntry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface RegisterRepository extends ReactiveMongoRepository<RegisterEntry, String> {
    Flux<RegisterEntry> findAllByDate(LocalDate date);
    Flux<RegisterEntry> findAllByProject(String projectId);
    Mono<RegisterEntry> findFirstByProjectAndDate(String projectId, LocalDate date);
}
