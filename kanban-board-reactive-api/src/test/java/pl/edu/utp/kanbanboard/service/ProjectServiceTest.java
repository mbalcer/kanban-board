package pl.edu.utp.kanbanboard.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.edu.utp.kanbanboard.model.Project;
import pl.edu.utp.kanbanboard.repository.ProjectRepository;
import pl.edu.utp.kanbanboard.service.impl.ProjectServiceImpl;
import pl.edu.utp.kanbanboard.util.ProjectTestUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
public class ProjectServiceTest {
    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Test
    public void testGetAll(){
        Project project1 = ProjectTestUtil.getProject1();
        Project project2 = ProjectTestUtil.getProject2();
        Project project3 = ProjectTestUtil.getProject3();
        Flux<Project> projectFlux = Flux.just(project1,project2,project3);

        Mockito.when(projectRepository.findAll())
                .thenReturn(projectFlux);

        Flux<Project> result = projectService.all();

        StepVerifier
                .create(result)
                .expectNext(project1)
                .expectNext(project2)
                .expectNext(project3)
                .verifyComplete();
    }

    @Test
    public void testCreate() {
        Project project = ProjectTestUtil.getEmptyProject();

        Mockito.when(projectRepository.save(project))
                .thenReturn(Mono.just(project));

        Mono<Project> result = projectService.create(project);

        StepVerifier
                .create(result)
                .consumeNextWith(next -> {
                    assertNotNull(next.getProjectId());
                })
                .verifyComplete();
    }

    @Test
    public void testUpdate() {
        Project project = ProjectTestUtil.getProject3();
        Project updateProject = ProjectTestUtil.getProject2();

        Mockito.when(projectRepository.findById(project.getProjectId()))
                .thenReturn(Mono.just(project));

        Mockito.when(projectRepository.save(Mockito.any(Project.class)))
                .thenReturn(Mono.just(updateProject));

        Mono<Project> result = projectService.update(project.getProjectId(), updateProject);

        StepVerifier
                .create(result)
                .expectNext(updateProject)
                .verifyComplete();
    }

}
