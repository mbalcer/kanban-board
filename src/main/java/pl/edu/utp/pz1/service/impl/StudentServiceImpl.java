package pl.edu.utp.pz1.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.edu.utp.pz1.model.Student;
import pl.edu.utp.pz1.repository.StudentRepository;
import pl.edu.utp.pz1.service.StudentService;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Optional<Student> findById(Integer studentId) {
        return studentRepository.findById(studentId);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Page<Student> getStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public Student create(Student student) {
        if (student.getStudentId() != null) {
            throw new IllegalArgumentException("");
        }

        return studentRepository.save(student);
    }

    @Override
    public Student update(Integer studentId, Student updatedStudent) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        Student student = studentOptional.orElseThrow(() -> new IllegalArgumentException(""));

        student.setFirstName(updatedStudent.getFirstName());
        student.setLastName(updatedStudent.getLastName());
        student.setEmail(updatedStudent.getEmail());
        student.setFullTime(updatedStudent.getFullTime());
        student.setIndexNumber(updatedStudent.getIndexNumber());

        return studentRepository.save(student);
    }

    @Override
    public void delete(Integer studentId) {
        if (studentRepository.existsById(studentId)) {
            studentRepository.deleteById(studentId);
        } else {
            throw new IllegalArgumentException("");
        }
    }
}
