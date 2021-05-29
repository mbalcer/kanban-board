package pl.edu.utp.pz1.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.edu.utp.pz1.exception.ProjectNotFoundException;
import pl.edu.utp.pz1.model.Project;
import pl.edu.utp.pz1.model.Student;
import pl.edu.utp.pz1.repository.ProjectRepository;
import pl.edu.utp.pz1.repository.TaskRepository;
import pl.edu.utp.pz1.service.ProjectService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Optional<Project> findById(Integer projectId) {
        return projectRepository.findById(projectId);
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public List<Project> findAllByUser(String email) {
        return projectRepository.findAllByStudentsEmail(email);
    }

    @Override
    public Page<Project> getProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Override
    public Project create(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project update(Integer id, Project updatedProject) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        Project project = projectOptional.orElseThrow(ProjectNotFoundException::new);

        project.setName(updatedProject.getName());
        project.setDescription(updatedProject.getDescription());
        project.setSubmitDateTime(updatedProject.getSubmitDateTime());

        return projectRepository.save(project);
    }

    @Override
    public Project addStudent(Integer id, Student student) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        Project project = projectOptional.orElseThrow(ProjectNotFoundException::new);
        project.getStudents().add(student);
        return projectRepository.save(project);
    }

    @Override
    public void delete(Integer id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        Project project = projectOptional.orElseThrow(ProjectNotFoundException::new);
        project.getTasks().forEach(taskRepository::delete);
        projectRepository.delete(project);
    }
}
