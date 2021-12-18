package pl.edu.utp.kanbanboard.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.edu.utp.kanbanboard.model.Student;
import pl.edu.utp.kanbanboard.repository.StudentRepository;
import pl.edu.utp.kanbanboard.service.impl.StudentServiceImpl;
import pl.edu.utp.kanbanboard.util.StudentTestUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class StudentServiceTest {
    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;

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

}
