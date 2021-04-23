package pl.edu.utp.pz1.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.edu.utp.pz1.model.Project;
import pl.edu.utp.pz1.repository.ProjectRepository;
import pl.edu.utp.pz1.service.ProjectService;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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
        Project project = projectOptional.orElseThrow(() -> new IllegalArgumentException(""));

        project.setName(updatedProject.getName());
        project.setDescription(updatedProject.getDescription());
        project.setCreateDateTime(updatedProject.getCreateDateTime());
        project.setSubmitDateTime(updatedProject.getSubmitDateTime());

        return projectRepository.save(project);
    }

    @Override
    public void delete(Integer id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("");
        }
    }
}
