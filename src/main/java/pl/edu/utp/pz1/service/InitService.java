package pl.edu.utp.pz1.service;

import org.springframework.stereotype.Service;
import pl.edu.utp.pz1.model.Project;
import pl.edu.utp.pz1.model.Student;
import pl.edu.utp.pz1.model.Task;
import pl.edu.utp.pz1.model.TaskState;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
public class InitService {

    private ProjectService projectService;
    private TaskService taskService;
    private StudentService studentService;

    public InitService(ProjectService projectService, TaskService taskService, StudentService studentService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.studentService = studentService;
    }

    @PostConstruct
    public void initializeData() {
        Student student1 = new Student("Jan", "Kowalski", "111000", true, "jankow@wp.pl", "Qwerty.1");
        Student student2 = new Student( "Andrzej", "Nowak", "111001", true, "andnow@wp.pl", "Qwerty.1");
        student1 = studentService.create(student1);
        student2 = studentService.create(student2);

        Project project1 = new Project("Programowanie zwinne", "opis");
        Project project2 = new Project("Programowanie obiektowe", "opis2");
        Project project3 = new Project("Programowanie współbieżne", "opis3");

        project1.setSubmitDateTime(LocalDateTime.now());
        project2.setSubmitDateTime(LocalDateTime.now());
        project3.setSubmitDateTime(LocalDateTime.now());

        project1.addStudent(student1);
        project1.addStudent(student2);
        project2.addStudent(student1);
        project3.addStudent(student1);

        project1 = projectService.create(project1);
        project2 = projectService.create(project2);
        project3 = projectService.create(project3);

        for (int i = 0; i < 10; i++) {
            Task task = new Task(null, "zadanie" + i, "opis" + i, i, TaskState.TODO, LocalDateTime.now(), project1, student1);
            taskService.create(task);
        }
    }
}
