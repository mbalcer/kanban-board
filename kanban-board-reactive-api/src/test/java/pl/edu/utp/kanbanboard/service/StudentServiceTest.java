package pl.edu.utp.kanbanboard.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.edu.utp.kanbanboard.model.Student;
import pl.edu.utp.kanbanboard.repository.StudentRepository;
import pl.edu.utp.kanbanboard.service.impl.StudentServiceImpl;
import pl.edu.utp.kanbanboard.util.StudentTestUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
public class StudentServiceTest {
    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testGetAll() {
        Student student1 = StudentTestUtil.getStudent1();
        Student student2 = StudentTestUtil.getStudent2();
        Student student3 = StudentTestUtil.getStudent3();
        Flux<Student> studentFlux = Flux.just(student1, student2, student3);

        Mockito.when(studentRepository.findAll())
                .thenReturn(studentFlux);

        Flux<Student> result = studentService.all();

        StepVerifier
                .create(result)
                .expectNext(student1)
                .expectNext(student2)
                .expectNext(student3)
                .verifyComplete();
    }

    @Test
    public void testGetByEmail() {
        Student student = StudentTestUtil.getStudent1();
        String testEmail = student.getEmail();

        Mockito.when(studentRepository.getByEmail(testEmail))
                .thenReturn(Mono.just(student));

        Mono<Student> result = studentService.getByEmail(testEmail);

        StepVerifier
                .create(result)
                .expectNext(student)
                .verifyComplete();
    }

    @Test
    public void testCreate() {
        Student student = StudentTestUtil.getEmptyStudent();
        String newPassword = "newPa$$word1!";

        Mockito.when(studentRepository.save(student))
                .thenReturn(Mono.just(student));

        Mockito.when(passwordEncoder.encode(student.getPassword()))
                .thenReturn(newPassword);

        Mono<Student> result = studentService.create(student);

        StepVerifier
                .create(result)
                .consumeNextWith(next -> {
                    assertNotNull(next.getStudentId());
                    assertEquals(next.getPassword(), newPassword);
                })
                .verifyComplete();
    }

    @Test
    public void testUpdate() {
        Student student = StudentTestUtil.getStudent3();
        Student updateStudent = StudentTestUtil.getStudent2();

        Mockito.when(studentRepository.findById(student.getStudentId()))
                .thenReturn(Mono.just(student));

        Mockito.when(studentRepository.save(Mockito.any(Student.class)))
                .thenReturn(Mono.just(updateStudent));

        Mono<Student> result = studentService.update(student.getStudentId(), updateStudent);

        StepVerifier
                .create(result)
                .expectNext(updateStudent)
                .verifyComplete();
    }

    @Test
    public void testUpdatePassword_passed() {
        Student student = StudentTestUtil.getStudent3();
        String newPassword = "newPa$$word1!";

        Mockito.when(studentRepository.findById(student.getStudentId()))
                .thenReturn(Mono.just(student));

        Mockito.when(passwordEncoder.matches(student.getPassword(), student.getPassword()))
                .thenReturn(true);

        Mockito.when(passwordEncoder.encode(newPassword))
                .thenReturn(newPassword);

        Mockito.when(studentRepository.save(student))
                .thenReturn(Mono.just(student));

        Mono<Student> result = studentService.updatePassword(student.getStudentId(), student.getPassword(), newPassword);

        StepVerifier
                .create(result)
                .consumeNextWith(next -> assertEquals(next.getPassword(), newPassword))
                .verifyComplete();
    }

    @Test
    public void testUpdatePassword_failed() {
        Student student = StudentTestUtil.getStudent3();
        String newPassword = "newPa$$word1!";

        Mockito.when(studentRepository.findById(student.getStudentId()))
                .thenReturn(Mono.just(student));

        Mockito.when(passwordEncoder.matches(student.getPassword(), student.getPassword()))
                .thenReturn(false);

        Mono<Student> result = studentService.updatePassword(student.getStudentId(), student.getPassword(), newPassword);

        StepVerifier
                .create(result)
                .verifyComplete();
    }

}
