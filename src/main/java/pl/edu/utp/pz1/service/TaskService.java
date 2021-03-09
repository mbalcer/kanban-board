package pl.edu.utp.pz1.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.edu.utp.pz1.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Optional<Task> findTask(Integer taskId);

    Page<Task> getTasks(Integer projectId, Pageable pageable);

    List<Task> getTasks(Integer projectId);

    Task createTask(Task task);

    Task updateTask(Integer taskId, Task updatedTask);

    boolean deleteTask(Integer taskId);

}
