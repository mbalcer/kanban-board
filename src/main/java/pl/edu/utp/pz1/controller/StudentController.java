package pl.edu.utp.pz1.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.utp.pz1.model.Student;
import pl.edu.utp.pz1.service.StudentService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer studentId) {
        return ResponseEntity.of(studentService.findById(studentId));
    }

    @GetMapping
    public Page<Student> getStudents(@RequestParam("page") int page, @RequestParam("size") int size) {
        return studentService.getStudents(PageRequest.of(page, size));
    }

    @GetMapping("/all")
    public List<Student> findAll() {
        return studentService.findAll();
    }

    @PostMapping
    public ResponseEntity<Student> createTask(@RequestBody Student student) {
        Student newStudent = studentService.create(student);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/" + newStudent.getStudentId()).build().toUri();
        return ResponseEntity.created(location).body(newStudent);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateTask(@PathVariable Integer studentId, @RequestBody Student student) {
        Student updatedStudent = studentService.update(studentId, student);

        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer studentId) {
        studentService.delete(studentId);
        return ResponseEntity.noContent().build();
    }
}
