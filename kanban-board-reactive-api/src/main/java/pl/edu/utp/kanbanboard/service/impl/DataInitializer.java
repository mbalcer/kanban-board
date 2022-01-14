package pl.edu.utp.kanbanboard.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.edu.utp.kanbanboard.model.Project;
import pl.edu.utp.kanbanboard.model.Student;
import pl.edu.utp.kanbanboard.model.Task;
import pl.edu.utp.kanbanboard.model.TaskState;
import pl.edu.utp.kanbanboard.repository.ProjectRepository;
import pl.edu.utp.kanbanboard.repository.StudentRepository;
import pl.edu.utp.kanbanboard.repository.TaskRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@Slf4j
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final StudentRepository studentRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private PasswordEncoder passwordEncoder;

    public DataInitializer(StudentRepository studentRepository,
                           ProjectRepository projectRepository,
                           TaskRepository taskRepository,
                           PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        taskRepository.deleteAll()
                .thenMany(Flux.just(5, 6, 7)
                        .map(number -> new Task(UUID.randomUUID().toString(), "Task " + number,
                                "Description " + number, number, TaskState.TODO,
                                LocalDateTime.now(), null))
                        .flatMap(taskRepository::save))
                .thenMany(taskRepository.findAll())
                .subscribe(task -> log.info("saving " + task.toString()));

        // TODO:  naprawa błędów...
        projectRepository.deleteAll()
                .thenMany(Flux.just(5, 6, 7)
                        .map(number -> new Project(UUID.randomUUID().toString(), "Project " + number,
                                "Description " + number, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(number),
//                                new HashSet<String>(Collections.singleton(taskRepository.findByName("Task " + number).blockFirst().getTaskId())),
                                new HashSet<>(),
                                new HashSet<>()))
                        .flatMap(projectRepository::save))
                .thenMany(projectRepository.findAll())
                .subscribe(project -> log.info("saving " + project.toString()));

        // TODO: naprawa błędów...
        studentRepository.deleteAll()
                .thenMany(Flux.just("Mateusz", "Szymon", "Kacper", "Karol")
                        .map(name -> new Student(UUID.randomUUID().toString(), name, name, "11133" + name.length(),
                                true, name + "@gmail.com", passwordEncoder.encode("123456789"),
//                                new HashSet<String>(Collections.singleton(projectRepository.findByName("Project " + name.length()).blockFirst().getProjectId())),
                                new HashSet<>()))
                        .flatMap(studentRepository::save))
                .thenMany(studentRepository.findAll())
                .subscribe(student -> log.info("saving " + student.toString()));
    }
}