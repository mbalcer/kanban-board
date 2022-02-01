package pl.edu.utp.kanbanboard.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.utp.kanbanboard.model.Project;
import pl.edu.utp.kanbanboard.model.Student;
import pl.edu.utp.kanbanboard.service.ProjectService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/project", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/all")
    public Flux<Project> getAllProjects() {
        return projectService.all();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Project>> getProjectById(@PathVariable String id) {
        return projectService.get(id)
                .map(project -> ResponseEntity.ok(project))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/user/{email}")
    public Flux<Project> getAllProjectsByUser(@PathVariable String email) {
        return projectService.allByUser(email);
    }

    @PostMapping
    public Mono<ResponseEntity<Project>> createProject(@RequestBody Project project) {
        return projectService.create(project)
                .map(s -> {
                    URI location = URI.create("/api/project/" + s.getProjectId());
                    return ResponseEntity.created(location).body(project);
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Project>> updateProject(@PathVariable String id, @RequestBody Project project) {
        return projectService.update(id, project)
                .map(s -> ResponseEntity.ok(s))
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
    }

    @PutMapping("/student/{projectId}")
    public Mono<ResponseEntity<Project>> addStudent(@PathVariable String projectId, @RequestBody Student student) {
        return projectService.addStudent(projectId, student.getStudentId())
                .map(s -> ResponseEntity.ok(s))
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteProject(@PathVariable String id) {
        return projectService.delete(id)
                .map(project -> ResponseEntity.noContent().build())
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
    }
}
