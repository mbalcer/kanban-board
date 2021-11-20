package pl.edu.utp.kanbanboard.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.edu.utp.kanbanboard.model.Student;
import pl.edu.utp.kanbanboard.service.StudentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<Student> getStudentById(@PathVariable String id) {
        return studentService.get(id);
    }

    @GetMapping("/email/{email}")
    public Mono<Student> getStudentByEmail(@PathVariable String email) {
        return studentService.getByEmail(email);
    }

    @PostMapping
    public Mono<Student> createStudent(@RequestBody Student student) {
        return studentService.create(student);
    }

    @PutMapping("/{id}")
    public Mono<Student> updateStudent(@PathVariable String id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable String id) {
        studentService.delete(id);
    }
}
