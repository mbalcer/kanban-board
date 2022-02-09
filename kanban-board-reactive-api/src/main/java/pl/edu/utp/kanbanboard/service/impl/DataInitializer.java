package pl.edu.utp.kanbanboard.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.utp.kanbanboard.model.*;
import pl.edu.utp.kanbanboard.repository.ProjectRepository;
import pl.edu.utp.kanbanboard.repository.RegisterRepository;
import pl.edu.utp.kanbanboard.repository.StudentRepository;
import pl.edu.utp.kanbanboard.repository.TaskRepository;
import pl.edu.utp.kanbanboard.service.RegisterService;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final StudentRepository studentRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final RegisterRepository registerRepository;
    private final RegisterService registerService;
    private PasswordEncoder passwordEncoder;

    public DataInitializer(StudentRepository studentRepository,
                           ProjectRepository projectRepository,
                           TaskRepository taskRepository,
                           RegisterRepository registerRepository,
                           RegisterService registerService,
                           PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.registerRepository = registerRepository;
        this.registerService = registerService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        studentRepository.deleteAll()
                .doOnSuccess(i -> projectRepository.deleteAll())
                .doOnSuccess(i -> taskRepository.deleteAll())
                .doOnSuccess(i -> registerRepository.deleteAll())
                .thenMany(Flux.just("matbal", "kardur", "szybet")
                        .map(name -> new Student(UUID.randomUUID().toString(), name, name, "11133" + name.length(),
                                true, name + "@pbs.edu.pl", passwordEncoder.encode("Qwerty.1"),
                                new HashSet<>())))
                .flatMap(studentRepository::save)
                .thenMany(studentRepository.findAll())
                .doOnNext(student -> log.info("saving " + student.toString()))
                .doOnNext(student -> Flux.just(1, 2, 3)
                        .map(number -> new Project(UUID.randomUUID().toString(), "Project " + number,
                                "Description " + number, LocalDateTime.now().minusDays(number),
                                LocalDateTime.now().minusHours(number), LocalDateTime.now().plusDays(number),
                                new HashSet<>(), new HashSet<>(Collections.singleton(student.getStudentId())), new HashSet<>()))
                        .flatMap(projectRepository::save)
                        .thenMany(projectRepository.findAllByStudentsContains(student.getStudentId()))
                        .doOnNext(project -> System.out.println("saving " + project.toString()))
                        .doOnNext(project -> Flux.just(
                                0, 4, 8, 12, 16, 20, 24, 28, 32, 36, 40,  // tasks to do       (11)
                                1, 5, 9, 13, 17, 21, 25,                  // tasks in progress  (7)
                                2, 6, 10, 14,                             // tasks in testing   (4)
                                3, 7, 11)                                 // tasks done         (3)
                                .map(number -> new Task(UUID.randomUUID().toString(), "Task " + number,
                                        "Description " + number, number, TaskState.values()[number % 4],
                                        LocalDateTime.now().minusDays(number), project.getProjectId(), student.getStudentId()))
                                .flatMap(taskRepository::save)
                                .thenMany(taskRepository.findAllByProject(project.getProjectId()))
                                .doOnNext(task -> System.out.println("saving " + task.toString()))
                                .subscribe())
                        .doOnNext(project -> Flux.just(1, 2, 3, 4, 5, 6)  // number of days before today
                                .map(number -> new RegisterEntry(UUID.randomUUID().toString(), LocalDate.now().minusDays(number),
                                        Map.of(TaskState.TODO, (11 / number),  // random number of tasks (but less or the same as today)
                                                TaskState.IN_PROGRESS, (6 / number),
                                                TaskState.TESTING, (3 / number),
                                                TaskState.DONE, (1 / number)), project.getProjectId()))
                                .flatMap(registerRepository::save)
                                .thenMany(registerRepository.findAllByProject(project.getProjectId()))
                                .doOnNext(register -> log.info("saving " + register.toString()))
                                .subscribe())
                        .subscribe())
                .subscribe();

        Flux.interval(Duration.ofHours(1))
                .doOnNext(next -> registerService.updateAll())
                .subscribe(next -> System.out.println("Flow register update..."));
    }
}