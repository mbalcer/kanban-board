package pl.edu.utp.pz1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.utp.pz1.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("SELECT t FROM Task t WHERE t.project.projectId = :projectId")
    Page<Task> findTasksByProjectId(@Param("projectId") Integer projectId, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.project.projectId = :projectId")
    List<Task> findTasksByProjectId(@Param("projectId") Integer projectId);

}
