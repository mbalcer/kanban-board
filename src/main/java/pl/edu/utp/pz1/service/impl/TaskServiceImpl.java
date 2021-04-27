package pl.edu.utp.pz1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.edu.utp.pz1.model.Task;
import pl.edu.utp.pz1.repository.TaskRepository;
import pl.edu.utp.pz1.service.TaskService;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Optional<Task> findTask(Integer taskId) {
        return taskRepository.findById(taskId);
    }

    @Override
    public Page<Task> getTasks(Integer projectId, Pageable pageable) {
        return taskRepository.findTasksByProjectId(projectId, pageable);
    }

    @Override
    public List<Task> getTasks(Integer projectId) {
        return taskRepository.findTasksByProjectId(projectId);
    }

    @Override
    public Task create(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task update(Integer taskId, Task updatedTask) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        Task task = optionalTask.orElseThrow(() -> new IllegalArgumentException("Object with given ID does not exist"));
        task.setName(updatedTask.getName());
        task.setDescription(updatedTask.getDescription());
        task.setSequence(updatedTask.getSequence());
        return taskRepository.save(task);
    }

    @Override
    public void delete(Integer taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
        } else {
            throw new IllegalArgumentException("Object with given ID does not exist");
        }
    }

}
