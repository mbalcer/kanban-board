package pl.edu.utp.kanbanboard.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.utp.kanbanboard.model.Task;
import pl.edu.utp.kanbanboard.service.TaskService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/task", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService projectService) {this.taskService = projectService;}

    @GetMapping("/all")
    public Flux<Task> getAllProjects() {
        return taskService.all();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Task>> getProjectById(@PathVariable String id) {
        return taskService.get(id)
                .map(task -> ResponseEntity.ok(task))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping
    public Mono<ResponseEntity<Task>> createProject(@RequestBody Task task) {
        return taskService.create(task)
                .map(s -> {
                    URI location = URI.create("/api/project/" + s.getTaskId());
                    return ResponseEntity.created(location).body(task);
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Task>> updateProject(@PathVariable String id, @RequestBody Task task) {
        return taskService.update(id, task)
                .map(s -> ResponseEntity.ok(s))
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> deleteProject(@PathVariable String id) {
        return taskService.delete(id)
                .map(task -> ResponseEntity.noContent().build())
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
    }
}
