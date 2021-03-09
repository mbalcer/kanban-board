package pl.edu.utp.pz1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.utp.pz1.model.Task;
import pl.edu.utp.pz1.service.TaskService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Task> findTaskById(@RequestParam("taskId") Integer taskId) {
        return ResponseEntity.of(taskService.findTask(taskId));
    }

    @GetMapping
    public Page<Task> getTasks(@RequestParam("projectId") Integer projectId, Pageable pageable) {
        return taskService.getTasks(projectId, pageable);
    }

    @GetMapping
    public List<Task> getTasks(@RequestParam("projectId") Integer projectId) {
        return taskService.getTasks(projectId);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .queryParam("taskId", createdTask.getTaskId()).build().toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Task> updateTask(@RequestParam("taskId") Integer taskId,
                                           @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(taskId, task);
        if (updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTask(@RequestParam("taskId") Integer taskId) {
        if (taskService.deleteTask(taskId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
