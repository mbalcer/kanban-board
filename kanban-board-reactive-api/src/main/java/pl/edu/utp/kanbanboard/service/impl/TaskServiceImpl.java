package pl.edu.utp.kanbanboard.service.impl;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.edu.utp.kanbanboard.event.TaskCreatedEvent;
import pl.edu.utp.kanbanboard.event.TaskEditedEvent;
import pl.edu.utp.kanbanboard.model.Task;
import pl.edu.utp.kanbanboard.repository.TaskRepository;
import pl.edu.utp.kanbanboard.service.RelationshipService;
import pl.edu.utp.kanbanboard.service.TaskService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final RelationshipService relationshipService;
    private final ApplicationEventPublisher publisher;

    public TaskServiceImpl(TaskRepository taskRepository, RelationshipService relationshipService, ApplicationEventPublisher publisher) {
        this.taskRepository = taskRepository;
        this.relationshipService = relationshipService;
        this.publisher = publisher;
    }

    @Override
    public Flux<Task> all() { return this.taskRepository.findAll(); }

    @Override
    public Mono<Task> get(String id) { return this.taskRepository.findById(id); }

    @Override
    public Mono<Task> create(Task newTask) {
        return Mono.just(newTask)
                .filterWhen(task -> task.getProjectId()!=null ? relationshipService.isExistProject(task.getProjectId()) : Mono.just(true))
                .filterWhen(task -> task.getStudentId()!=null ? relationshipService.isExistStudent(task.getStudentId()) : Mono.just(true))
                .map(task -> {
                    task.setTaskId(UUID.randomUUID().toString());
                    task.setCreateDateTime(LocalDateTime.now());
                    return task;
                })
                .flatMap(taskRepository::save)
                .doOnSuccess(task -> publisher.publishEvent(new TaskCreatedEvent(task)))
                .doOnError(error -> {
                    throw new IllegalArgumentException("The project id or student id that you entered does not exist", error);
                });
    }

    @Override
    public Mono<Task> update(String id, Task updateTask) {
        return this.taskRepository
                .findById(id)
                .filterWhen(task -> task.getStudentId()!=null ? relationshipService.isExistStudent(task.getStudentId()) : Mono.just(true))
                .doOnNext(task -> {
                    task.setName(updateTask.getName());
                    task.setDescription(updateTask.getDescription());
                    task.setState(updateTask.getState());
                    task.setSequence(updateTask.getSequence());
                    task.setStudentId(updateTask.getStudentId());
                })
                .flatMap(this.taskRepository::save)
                .doOnSuccess(task -> publisher.publishEvent(new TaskEditedEvent(task, "updated")))
                .doOnError(error -> {
                    throw new IllegalArgumentException("The student id that you entered does not exist", error);
                });
    }

    @Override
    public Mono<Task> delete(String id) {
        return this.taskRepository
                .findById(id)
                .flatMap(s -> this.taskRepository.deleteById(s.getTaskId()).thenReturn(s))
                .doOnSuccess(task -> publisher.publishEvent(new TaskEditedEvent(task, "deleted")));
    }
}
