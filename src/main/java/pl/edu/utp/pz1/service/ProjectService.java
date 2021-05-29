package pl.edu.utp.pz1.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.edu.utp.pz1.model.Project;
import pl.edu.utp.pz1.model.Student;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    Optional<Project> findById(Integer projectId);

    List<Project> findAll();

    List<Project> findAllByUser(String email);

    Page<Project> getProjects(Pageable pageable);

    Project create(Project project);

    Project update(Integer id, Project updatedProject);

    Project addStudent(Integer id, Student student);

    void delete(Integer id);
}
