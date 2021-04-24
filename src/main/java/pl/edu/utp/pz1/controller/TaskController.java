package pl.edu.utp.pz1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.utp.pz1.model.Task;
import pl.edu.utp.pz1.service.TaskService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/task")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Integer taskId) {
        return ResponseEntity.of(taskService.findTask(taskId));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getTasksByProjectId(@PathVariable Integer projectId) {
        return ResponseEntity.ok(taskService.getTasks(projectId));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.create(task);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .queryParam("taskId", createdTask.getTaskId()).build().toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Task> updateTask(@RequestParam("taskId") Integer taskId,
                                           @RequestBody Task task) {
        Task updatedTask = taskService.update(taskId, task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTask(@RequestParam("taskId") Integer taskId) {
        taskService.delete(taskId);
        return ResponseEntity.noContent().build();
    }

}
