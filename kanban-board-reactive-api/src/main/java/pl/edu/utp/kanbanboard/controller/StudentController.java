package pl.edu.utp.kanbanboard.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.utp.kanbanboard.model.Student;
import pl.edu.utp.kanbanboard.service.StudentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/student", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/all")
    public Flux<Student> getAllStudents() {
        return studentService.all();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Student>> getStudentById(@PathVariable String id) {
        return studentService.get(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<Student>> getStudentByEmail(@PathVariable String email) {
        return studentService.getByEmail(email)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping
    public Mono<ResponseEntity<Student>> createStudent(@RequestBody Student student) {
        return studentService.create(student)
                .map(s -> {
                    URI location = URI.create("/api/student/" + s.getStudentId());
                    return ResponseEntity.created(location).body(student);
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Student>> updateStudent(@PathVariable String id, @RequestBody Student student) {
        return studentService.update(id, student)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
    }

    @PutMapping("/password/{studentId}")
    public Mono<ResponseEntity<Student>> updatePassword(@PathVariable String studentId, @RequestBody ObjectNode objectNode) {
        String currentPassword = objectNode.get("currentPassword").asText();
        String newPassword = objectNode.get("newPassword").asText();
        return studentService.updatePassword(studentId, currentPassword, newPassword)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteStudent(@PathVariable String id) {
        return studentService.delete(id)
                .map(student -> ResponseEntity.noContent().build())
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
    }
}
