package pl.edu.utp.pz1.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.edu.utp.pz1.exception.ProjectNotFoundException;
import pl.edu.utp.pz1.exception.TaskNotFoundException;
import pl.edu.utp.pz1.model.Task;
import pl.edu.utp.pz1.repository.ProjectRepository;
import pl.edu.utp.pz1.repository.TaskRepository;
import pl.edu.utp.pz1.service.TaskService;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;
    private SimpMessagingTemplate simpMessagingTemplate;

    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
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
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException();
        }
        return taskRepository.findTasksByProjectId(projectId);
    }

    @Override
    public Task create(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task update(Integer taskId, Task updatedTask) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        Task task = optionalTask.orElseThrow(TaskNotFoundException::new);
        task.setName(updatedTask.getName());
        task.setDescription(updatedTask.getDescription());
        task.setState(updatedTask.getState());
        task.setSequence(updatedTask.getSequence());
        task.setStudent(updatedTask.getStudent());
        Task saveTask = taskRepository.save(task);

        simpMessagingTemplate.convertAndSend("/task/" + taskId, saveTask);
        return saveTask;
    }

    @Override
    public void delete(Integer taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);

            simpMessagingTemplate.convertAndSend("/task/" + taskId, "deleted");
        } else {
            throw new TaskNotFoundException();
        }
    }

}
