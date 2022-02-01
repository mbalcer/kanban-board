package pl.edu.utp.kanbanboard.service;

import pl.edu.utp.kanbanboard.model.Student;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentService {
    Flux<Student> all();

    Mono<Student> get(String id);

    Mono<Student> getByEmail(String email);

    Mono<Student> create(Student newStudent);

    Mono<Student> update(String id, Student updateStudent);

    Mono<Student> updatePassword(String studentId, String currentPassword, String newPassword);

    Mono<Student> delete(String id);
}
