package pl.edu.utp.pz1.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import pl.edu.utp.pz1.exception.*;
import pl.edu.utp.pz1.model.Student;
import pl.edu.utp.pz1.model.Task;
import pl.edu.utp.pz1.model.TaskState;
import pl.edu.utp.pz1.repository.ProjectRepository;
import pl.edu.utp.pz1.repository.TaskRepository;
import pl.edu.utp.pz1.service.impl.TaskServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Test
    public void findTask() {
        Task task = new Task();
        task.setTaskId(4);
        task.setName("Task name");

        when(taskRepository.findById(task.getTaskId())).thenReturn(Optional.of(task));
        Task actual = taskService.findTask(task.getTaskId()).orElse(null);

        assertNotNull(actual);
        assertEquals(task.getName(), task.getName());
        verify(taskRepository, times(1)).findById(task.getTaskId());
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void getTasks_withPageable() {
        int page = 5;
        int size = 15;
        Pageable pageable = PageRequest.of(page, size);
        Integer projectId = 2;

        when(taskRepository.findTasksByProjectId(projectId, pageable))
                .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));
        Page<Task> taskPage = taskService.getTasks(projectId, pageable);

        assertEquals(page, taskPage.getPageable().getPageNumber());
        assertEquals(size, taskPage.getPageable().getPageSize());
        verify(taskRepository, times(1)).findTasksByProjectId(projectId, pageable);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void getTasks_withoutPageable_whenSuccess() {
        Task task1 = new Task();
        task1.setTaskId(4);
        task1.setName("Task 1 name");

        Task task2 = new Task();
        task2.setTaskId(5);
        task2.setName("Task 2 name");

        List<Task> given = Arrays.asList(task1, task2);
        Integer projectId = 2;

        when(projectRepository.existsById(projectId)).thenReturn(true);
        when(taskRepository.findTasksByProjectId(projectId)).thenReturn(given);

        List<Task> actual = taskService.getTasks(projectId);

        assertEquals(given.size(), actual.size());
        assertThat(actual, containsInAnyOrder(task1, task2));
        verify(projectRepository, times(1)).existsById(projectId);
        verify(taskRepository, times(1)).findTasksByProjectId(projectId);
        verifyNoMoreInteractions(projectRepository);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void getTasks_withoutPageable_whenProjectNotFound() {
        Integer projectId = 2;

        when(projectRepository.existsById(projectId)).thenReturn(false);
        assertThrows(ProjectNotFoundException.class, () -> taskService.getTasks(projectId));

        verify(projectRepository, times(1)).existsById(projectId);
        verify(taskRepository, times(0)).findTasksByProjectId(projectId);
        verifyNoMoreInteractions(projectRepository);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void createTask() {
        Task given = new Task();
        given.setName("Task name");
        given.setCreateDateTime(LocalDateTime.now());

        when(taskRepository.save(given)).thenReturn(given);
        Task actual = taskService.create(given);

        assertEquals(given.getName(), actual.getName());
        assertEquals(given.getCreateDateTime(), actual.getCreateDateTime());
        verify(taskRepository, times(1)).save(given);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void updateTask_whenFound() {
        Student newStudent = new Student();

        Task givenTask = new Task();
        givenTask.setTaskId(1);
        givenTask.setName(null);
        givenTask.setDescription(null);
        givenTask.setState(null);
        givenTask.setSequence(null);
        givenTask.setStudent(null);
        givenTask.setCreateDateTime(null);

        Task newTask = new Task();
        newTask.setTaskId(2);
        newTask.setName("Task name");
        newTask.setDescription("Description");
        newTask.setState(TaskState.TODO);
        newTask.setSequence(1);
        newTask.setStudent(newStudent);
        newTask.setCreateDateTime(LocalDateTime.now());

        when(taskRepository.findById(givenTask.getTaskId())).thenReturn(Optional.of(givenTask));
        when(taskRepository.save(givenTask)).thenReturn(givenTask);
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), anyString());

        Task actual = taskService.update(givenTask.getTaskId(), newTask);

        assertEquals(newTask.getName(), actual.getName());
        assertEquals(newTask.getDescription(), actual.getDescription());
        assertEquals(newTask.getState(), actual.getState());
        assertEquals(newTask.getSequence(), actual.getSequence());
        assertEquals(newTask.getStudent(), actual.getStudent());
        assertNotEquals(newTask.getCreateDateTime(), actual.getCreateDateTime());
        verify(taskRepository, times(1)).findById(givenTask.getTaskId());
        verify(taskRepository, times(1)).save(givenTask);
        verify(simpMessagingTemplate, times(1)).convertAndSend(anyString(), anyString());
        verifyNoMoreInteractions(taskRepository);
        verifyNoMoreInteractions(simpMessagingTemplate);
    }

    @Test
    public void updateTask_whenNotFound() {
        Student newStudent = new Student();

        Task givenTask = new Task();
        givenTask.setTaskId(1);
        givenTask.setName(null);
        givenTask.setDescription(null);
        givenTask.setState(null);
        givenTask.setSequence(null);
        givenTask.setStudent(null);
        givenTask.setCreateDateTime(null);

        Task newTask = new Task();
        newTask.setTaskId(2);
        newTask.setName("Task name");
        newTask.setDescription("Description");
        newTask.setState(TaskState.TODO);
        newTask.setSequence(1);
        newTask.setStudent(newStudent);
        newTask.setCreateDateTime(LocalDateTime.now());

        when(taskRepository.findById(givenTask.getTaskId())).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.update(givenTask.getTaskId(), newTask));

        assertNotEquals(newTask.getName(), givenTask.getName());
        assertNotEquals(newTask.getDescription(), givenTask.getDescription());
        assertNotEquals(newTask.getState(), givenTask.getState());
        assertNotEquals(newTask.getSequence(), givenTask.getSequence());
        assertNotEquals(newTask.getStudent(), givenTask.getStudent());
        assertNotEquals(newTask.getCreateDateTime(), givenTask.getCreateDateTime());
        verify(taskRepository, times(1)).findById(givenTask.getTaskId());
        verify(taskRepository, times(0)).save(givenTask);
        verify(simpMessagingTemplate, times(0)).convertAndSend(anyString(), anyString());
        verifyNoMoreInteractions(taskRepository);
        verifyNoMoreInteractions(simpMessagingTemplate);
    }

    @Test
    public void deleteTask_whenFound() {
        Student given = new Student();
        given.setStudentId(2);

        when(taskRepository.existsById(given.getStudentId())).thenReturn(true);
        doNothing().when(taskRepository).deleteById(given.getStudentId());
        doNothing().when(simpMessagingTemplate).convertAndSend(anyString(), anyString());

        taskService.delete(given.getStudentId());

        verify(taskRepository, times(1)).existsById(given.getStudentId());
        verify(taskRepository, times(1)).deleteById(given.getStudentId());
        verify(simpMessagingTemplate, times(1)).convertAndSend(anyString(), anyString());
        verifyNoMoreInteractions(taskRepository);
        verifyNoMoreInteractions(simpMessagingTemplate);
    }

    @Test
    public void deleteTask_whenNotFound() {
        Student given = new Student();
        given.setStudentId(2);

        when(taskRepository.existsById(given.getStudentId())).thenReturn(true);
        assertThrows(TaskNotFoundException.class, () -> taskService.delete(given.getStudentId()));

        verify(taskRepository, times(1)).existsById(given.getStudentId());
        verify(taskRepository, times(0)).deleteById(given.getStudentId());
        verify(simpMessagingTemplate, times(0)).convertAndSend(anyString(), anyString());
        verifyNoMoreInteractions(taskRepository);
        verifyNoMoreInteractions(simpMessagingTemplate);
    }

}
