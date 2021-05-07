package pl.edu.utp.pz1.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.edu.utp.pz1.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Optional<Student> findById(Integer studentId);

    List<Student> findAll();

    Page<Student> getStudents(Pageable pageable);

    Student create(Student student);

    Student update(Integer studentId, Student updatedStudent);

    void delete(Integer studentId);

    Optional<Student> findByEmail(String email);
}
