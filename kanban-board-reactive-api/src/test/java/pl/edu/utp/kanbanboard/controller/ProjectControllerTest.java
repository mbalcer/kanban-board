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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import pl.edu.utp.kanbanboard.model.Project;
import pl.edu.utp.kanbanboard.repository.ProjectRepository;
import pl.edu.utp.kanbanboard.service.impl.ProjectServiceImpl;
import pl.edu.utp.kanbanboard.util.ProjectTestUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ProjectController.class,
        excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@Import(ProjectServiceImpl.class)
public class ProjectControllerTest {
    @MockBean
    ProjectRepository repository;

    @Autowired
    private WebTestClient webClient;

    private static final String API_PROJECT = "/api/project";

    @Test
    void testGetAllProjects(){
        Project project1 = ProjectTestUtil.getProject1();
        Project project2 = ProjectTestUtil.getProject2();
        Project project3 = ProjectTestUtil.getProject3();

        Flux<Project> projectFlux = Flux.just(project1,project2,project3);

        Mockito.when(repository.findAll()).
                thenReturn(projectFlux);

        webClient.get()
                .uri(API_PROJECT + "/all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Project.class)
                .hasSize(3)
                .consumeWith(response -> {
                    assertNotNull(response.getResponseBody());
                    Flux<Project> result = Flux.fromIterable(response.getResponseBody());
                    StepVerifier.create(result.log())
                            .expectNext(project1)
                            .expectNext(project2)
                            .expectNext(project3)
                            .verifyComplete();
                });

    }

    @Test
    void testGetProjectById() {
        Project project1 = ProjectTestUtil.getProject1();
        Mono<Project> projectMono = Mono.just(project1);

        Mockito.when(repository.findById(project1.getProjectId()))
                .thenReturn(projectMono);

        webClient.get()
                .uri(API_PROJECT + "/{id}", project1.getProjectId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Project.class)
                .consumeWith(response -> {
                   assertNotNull(response.getResponseBody());
                   Mono<Project> result = Mono.just(response.getResponseBody());
                   StepVerifier.create(result.log())
                           .expectNext(project1)
                           .verifyComplete();
                });
    }

    @Test
    void testUpdateProject() {
        Project project = ProjectTestUtil.getProject1();
        Project updateProject = ProjectTestUtil.getProject2();

        Project expectedProject = ProjectTestUtil.getProject2();
        expectedProject.setProjectId(project.getProjectId());

        Mockito.when(repository.save(Mockito.any(Project.class)))
                .thenReturn(Mono.just(project));

        Mockito.when(repository.save(Mockito.any(Project.class)))
                .thenReturn(Mono.just(updateProject));

        webClient.put()
                .uri(API_PROJECT + "/{id}", project.getProjectId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(updateProject))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Project.class)
                .consumeWith(response -> Mockito.verify(repository, Mockito.times(1)).save(expectedProject));
    }

    @Test
    void testUpdateProject_badRequest() {
        Project project = ProjectTestUtil.getProject1();
        Project updateProject = ProjectTestUtil.getProject2();

        Mockito.when(repository.findById(project.getProjectId()))
                .thenReturn(Mono.empty());

        webClient.put()
                .uri(API_PROJECT + "/{id}", project.getProjectId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(updateProject))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testDeleteProject() {
        Project projectToDelete = ProjectTestUtil.getProject3();

        Mockito.when(repository.findById(projectToDelete.getProjectId()))
                .thenReturn(Mono.just(projectToDelete));

        Mockito.when(repository.deleteById(projectToDelete.getProjectId()))
                .thenReturn(Mono.when());

        webClient.delete()
                .uri(API_PROJECT + "/{id}", projectToDelete.getProjectId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteProject_badRequest() {
        Project projectToDelete = ProjectTestUtil.getProject1();
        Mockito.when(repository.findById(projectToDelete.getProjectId()))
                .thenReturn(Mono.empty());

        webClient.delete()
                .uri(API_PROJECT + "/{id}", projectToDelete.getProjectId())
                .exchange()
                .expectStatus().isBadRequest();
    }
}
