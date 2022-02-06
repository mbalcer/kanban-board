package pl.edu.utp.kanbanboard.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.edu.utp.kanbanboard.model.Task;
import pl.edu.utp.kanbanboard.repository.TaskRepository;
import pl.edu.utp.kanbanboard.service.impl.TaskServiceImpl;
import pl.edu.utp.kanbanboard.util.TaskTestUntil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
public class TaskServiceTest {
    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Test
    public void testGetAll() {
        Task task1 = TaskTestUntil.generateTask1();
        Task task2 = TaskTestUntil.generateTask2();
        Task task3 = TaskTestUntil.generateTask3();
        Flux<Task> taskFlux = Flux.just(task1,task2,task3);

        Mockito.when(taskRepository.findAll())
                .thenReturn(taskFlux);

        Flux<Task> result = taskService.all();

        StepVerifier
                .create(result)
                .expectNext(task1)
                .expectNext(task2)
                .expectNext(task3)
                .verifyComplete();
    }

    @Test
    public void testCreate(){
        Task task = TaskTestUntil.getEmptyTask();

        Mockito.when(taskRepository.save(task))
                .thenReturn(Mono.just(task));

        Mono<Task> result = taskService.create(task);

        StepVerifier
                .create(result)
                .consumeNextWith(next -> {
                    assertNotNull(next.getTaskId());
                })
                .verifyComplete();
    }

    @Test
    public void testUpdate() {
        Task task = TaskTestUntil.generateTask3();
        Task updateTask = TaskTestUntil.generateTask2();

        Mockito.when(taskRepository.findById(task.getTaskId()))
                .thenReturn(Mono.just(task));

        Mockito.when(taskRepository.save(Mockito.any(Task.class)))
                .thenReturn(Mono.just(updateTask));

        Mono<Task> result = taskService.update(task.getTaskId(), updateTask);

        StepVerifier
                .create(result)
                .expectNext(updateTask)
                .verifyComplete();
    }
}
