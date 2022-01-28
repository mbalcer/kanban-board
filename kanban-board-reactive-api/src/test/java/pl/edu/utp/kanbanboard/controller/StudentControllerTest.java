package pl.edu.utp.kanbanboard.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import pl.edu.utp.kanbanboard.model.Student;
import pl.edu.utp.kanbanboard.repository.StudentRepository;
import pl.edu.utp.kanbanboard.service.impl.StudentServiceImpl;
import pl.edu.utp.kanbanboard.util.StudentTestUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = StudentController.class,
        excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@Import(StudentServiceImpl.class)
public class StudentControllerTest {
    @MockBean
    StudentRepository repository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebTestClient webClient;

    private static final String API_STUDENT = "/api/student";

    @Test
    void testGetAllStudents() {
        Student student1 = StudentTestUtil.getStudent1();
        Student student2 = StudentTestUtil.getStudent2();
        Student student3 = StudentTestUtil.getStudent3();

        Flux<Student> studentFlux = Flux.just(student1, student2, student3);

        Mockito.when(repository.findAll())
                .thenReturn(studentFlux);

        webClient.get()
                .uri(API_STUDENT + "/all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Student.class)
                .hasSize(3)
                .consumeWith(response -> {
                    assertNotNull(response.getResponseBody());
                    Flux<Student> result = Flux.fromIterable(response.getResponseBody());
                    StepVerifier.create(result.log())
                            .expectNext(student1)
                            .expectNext(student2)
                            .expectNext(student3)
                            .verifyComplete();
                });
    }

    @Test
    void testGetStudentById() {
        Student student1 = StudentTestUtil.getStudent1();
        Mono<Student> studentMono = Mono.just(student1);

        Mockito.when(repository.findById(student1.getStudentId()))
                .thenReturn(studentMono);

        webClient.get()
                .uri(API_STUDENT + "/{id}", student1.getStudentId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Student.class)
                .consumeWith(response -> {
                    assertNotNull(response.getResponseBody());
                    Mono<Student> result = Mono.just(response.getResponseBody());
                    StepVerifier.create(result.log())
                            .expectNext(student1)
                            .verifyComplete();
                });
    }

    @Test
    void testGetStudentByEmail() {
        Student student2 = StudentTestUtil.getStudent2();
        Mono<Student> studentMono = Mono.just(student2);

        Mockito.when(repository.getByEmail(student2.getEmail()))
                .thenReturn(studentMono);

        webClient.get()
                .uri(API_STUDENT + "/email/{email}", student2.getEmail())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Student.class)
                .consumeWith(response -> {
                    assertNotNull(response.getResponseBody());
                    Mono<Student> result = Mono.just(response.getResponseBody());
                    StepVerifier.create(result.log())
                            .expectNext(student2)
                            .verifyComplete();
                });
    }

    @Test
    void testGetStudentByEmail_notFound() {
        String email = "newUser@o2.pl";

        Mockito.when(repository.getByEmail(email))
                .thenReturn(Mono.empty());

        webClient.get()
                .uri(API_STUDENT + "/email/{email}", email)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateStudent() {
        Student student = StudentTestUtil.getEmptyStudent();
        Mono<Student> studentMono = Mono.just(student);
        String newPassword = "newPa$$word1!";

        Mockito.when(repository.save(Mockito.any(Student.class)))
                .thenReturn(studentMono);

        Mockito.when(passwordEncoder.encode(student.getPassword()))
                .thenReturn(newPassword);

        webClient.post()
                .uri(API_STUDENT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(student))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Student.class)
                .consumeWith(response -> {
                    assertNotNull(response.getResponseBody());
                    assertNotNull(response.getResponseBody().getStudentId());
                    assertEquals(response.getResponseBody().getPassword(), newPassword);
                    Mockito.verify(repository, Mockito.times(1)).save(response.getResponseBody());
                });
    }

    @Test
    void testUpdateStudent() {
        Student student = StudentTestUtil.getStudent1();
        Student updateStudent = StudentTestUtil.getStudent2();

        Student expectedStudent = StudentTestUtil.getStudent2();
        expectedStudent.setStudentId(student.getStudentId());
        expectedStudent.setPassword(student.getPassword());

        Mockito.when(repository.findById(student.getStudentId()))
                .thenReturn(Mono.just(student));

        Mockito.when(repository.save(Mockito.any(Student.class)))
                .thenReturn(Mono.just(updateStudent));

        webClient.put()
                .uri(API_STUDENT + "/{id}", student.getStudentId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(updateStudent))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Student.class)
                .consumeWith(response -> Mockito.verify(repository, Mockito.times(1)).save(expectedStudent));
    }

    @Test
    void testUpdateStudent_badRequest() {
        Student student = StudentTestUtil.getStudent1();
        Student updateStudent = StudentTestUtil.getStudent2();

        Mockito.when(repository.findById(student.getStudentId()))
                .thenReturn(Mono.empty());

        webClient.put()
                .uri(API_STUDENT + "/{id}", student.getStudentId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(updateStudent))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdatePassword() {
        Student student = StudentTestUtil.getStudent3();
        String currentPassword = student.getPassword();
        String newPassword = "newPa$$word1!";

        Mockito.when(repository.findById(student.getStudentId()))
                .thenReturn(Mono.just(student));

        Mockito.when(passwordEncoder.matches(currentPassword, currentPassword))
                .thenReturn(true);

        Mockito.when(passwordEncoder.encode(newPassword))
                .thenReturn(newPassword);

        Mockito.when(repository.save(student))
                .thenReturn(Mono.just(student));

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("currentPassword", currentPassword);
        bodyMap.put("newPassword", newPassword);

        webClient.put()
                .uri(API_STUDENT + "/password/{studentId}", student.getStudentId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bodyMap))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Student.class)
                .consumeWith(response -> {
                    assertNotNull(response.getResponseBody());
                    assertEquals(response.getResponseBody().getPassword(), newPassword);
                    Mockito.verify(repository, Mockito.times(1)).save(response.getResponseBody());
                    Mockito.verify(passwordEncoder, Mockito.times(1)).matches(currentPassword, currentPassword);
                    Mockito.verify(passwordEncoder, Mockito.times(1)).encode(newPassword);
                });
    }

    @Test
    void testUpdatePassword_notFound() {
        Student student = StudentTestUtil.getStudent3();
        String currentPassword = student.getPassword();
        String newPassword = "newPa$$word1!";

        Mockito.when(repository.findById(student.getStudentId()))
                .thenReturn(Mono.empty());

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("currentPassword", currentPassword);
        bodyMap.put("newPassword", newPassword);

        webClient.put()
                .uri(API_STUDENT + "/password/{studentId}", student.getStudentId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bodyMap))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(response -> Mockito.verify(repository, Mockito.times(1)).findById(student.getStudentId()));
    }

    @Test
    void testUpdatePassword_incorrectPassword() {
        Student student = StudentTestUtil.getStudent3();
        String currentPassword = student.getPassword();
        String newPassword = "newPa$$word1!";

        Mockito.when(repository.findById(student.getStudentId()))
                .thenReturn(Mono.just(student));

        Mockito.when(passwordEncoder.matches(currentPassword, currentPassword))
                .thenReturn(false);

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("currentPassword", currentPassword);
        bodyMap.put("newPassword", newPassword);

        webClient.put()
                .uri(API_STUDENT + "/password/{studentId}", student.getStudentId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bodyMap))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(response -> Mockito.verify(passwordEncoder, Mockito.times(1)).matches(currentPassword, currentPassword));
    }

    @Test
    void testDeleteStudent() {
        Student studentToDelete = StudentTestUtil.getStudent3();

        Mockito.when(repository.findById(studentToDelete.getStudentId()))
                .thenReturn(Mono.just(studentToDelete));

        Mockito.when(repository.deleteById(studentToDelete.getStudentId()))
                .thenReturn(Mono.when());

        webClient.delete()
                .uri(API_STUDENT + "/{id}", studentToDelete.getStudentId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteStudent_badRequest() {
        Student studentToDelete = StudentTestUtil.getStudent1();
        Mockito.when(repository.findById(studentToDelete.getStudentId()))
                .thenReturn(Mono.empty());

        webClient.delete()
                .uri(API_STUDENT + "/{id}", studentToDelete.getStudentId())
                .exchange()
                .expectStatus().isBadRequest();
    }
}
