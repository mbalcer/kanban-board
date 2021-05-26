package pl.edu.utp.pz1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.utp.pz1.model.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Page<Project> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Project> findByNameContainingIgnoreCase(String name);

    List<Project> findAllByStudentsEmail(String email);
}
