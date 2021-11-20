package pl.edu.utp.kanbanboard.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.edu.utp.kanbanboard.model.Student;
import pl.edu.utp.kanbanboard.repository.StudentRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Slf4j
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final StudentRepository studentRepository;

    public DataInitializer(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        studentRepository.deleteAll()
                .thenMany(Flux.just("Mateusz", "Szymon", "Kacper", "Karol")
                        .map(name -> new Student(UUID.randomUUID().toString(), name, name, "11133" + name.length(), true, name + "@gmail.com", "123456789"))
                        .flatMap(studentRepository::save))
                .thenMany(studentRepository.findAll())
                .subscribe(student -> log.info("saving " + student.toString()));
    }
}