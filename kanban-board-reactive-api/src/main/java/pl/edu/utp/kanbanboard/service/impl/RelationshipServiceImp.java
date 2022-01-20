package pl.edu.utp.kanbanboard.service.impl;

import org.springframework.stereotype.Service;
import pl.edu.utp.kanbanboard.repository.ProjectRepository;
import pl.edu.utp.kanbanboard.repository.StudentRepository;
import pl.edu.utp.kanbanboard.repository.TaskRepository;
import pl.edu.utp.kanbanboard.service.RelationshipService;
import reactor.core.publisher.Mono;

@Service
public class RelationshipServiceImp implements RelationshipService {
    private final ProjectRepository projectRepository;
    private final StudentRepository studentRepository;
    private final TaskRepository taskRepository;

    public RelationshipServiceImp(ProjectRepository projectRepository, StudentRepository studentRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.studentRepository = studentRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Mono<Boolean> isExistProject(String projectId) {
        return projectRepository.existsById(projectId);
    }

    @Override
    public Mono<Boolean> isExistTask(String taskId) {
        return taskRepository.existsById(taskId);
    }

    @Override
    public Mono<Boolean> isExistStudent(String studentId) {
        return studentRepository.existsById(studentId);
    }
}
