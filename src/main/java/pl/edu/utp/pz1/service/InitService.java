package pl.edu.utp.pz1.service;

import org.springframework.stereotype.Service;
import pl.edu.utp.pz1.model.Project;
import pl.edu.utp.pz1.model.Student;
import pl.edu.utp.pz1.model.Task;
import pl.edu.utp.pz1.model.TaskState;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Set;

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
        Project project1 = new Project("Programowanie zwinne", "opis");
        Project project2 = new Project("Programowanie obiektowe", "opis2");
        Project project3 = new Project("Programowanie współbieżne", "opis3");

        project1 = projectService.create(project1);
        project2 = projectService.create(project2);
        project3 = projectService.create(project3);

        for (int i = 0; i < 10; i++) {
            Task task = new Task(null, "zadanie" + i, "opis" + i, i, TaskState.TODO, LocalDateTime.now(), project1);
            taskService.create(task);
        }

        Student student = new Student(null, "Jan", "Kowalski", "111000", "jankow@wp.pl", true, Set.of(project1, project2, project3));
        studentService.create(student);
    }
}
