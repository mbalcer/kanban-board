package pl.edu.utp.pz1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.edu.utp.pz1.model.Project;
import pl.edu.utp.pz1.model.Task;
import pl.edu.utp.pz1.service.TaskService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(setupBefore = TestExecutionEvent.TEST_EXECUTION)
public class TaskControllerIT {

    private final String apiPath = "/api/task";

    @MockBean
    private TaskService taskService;

    @Autowired
    private MockMvc mockMvc;

    private JacksonTester<Task> jacksonTask;

    @Test
    public void getTaskById_whenFound() throws Exception {
        Task task = new Task();
        task.setTaskId(4);
        task.setName("Task name");

        when(taskService.findTask(task.getTaskId())).thenReturn(Optional.of(task));

        mockMvc.perform(get(apiPath + "/{taskId}", task.getTaskId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(task.getTaskId()))
                .andExpect(jsonPath("$.name").value(task.getName()));

        verify(taskService, times(1)).findTask(task.getTaskId());
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void getTaskById_whenNotFound() throws Exception {
        when(taskService.findTask(any())).thenReturn(Optional.empty());

        mockMvc.perform(get(apiPath + "/{taskId}", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).findTask(any());
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void getTasksByProjectId() throws Exception {
        Project project = new Project();
        project.setProjectId(3);
        project.setName("Project name");

        Task task1 = new Task();
        task1.setTaskId(4);
        task1.setName("Task 1 name");

        Task task2 = new Task();
        task2.setTaskId(5);
        task2.setName("Task 2 name");

        when(taskService.getTasks(project.getProjectId())).thenReturn(Arrays.asList(task1, task2));

        mockMvc.perform(get(apiPath + "/project/{projectId}", project.getProjectId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]").exists())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].taskId").value(task1.getTaskId()))
                .andExpect(jsonPath("$.[0].name").value(task1.getName()))
                .andExpect(jsonPath("$.[1].taskId").value(task2.getTaskId()))
                .andExpect(jsonPath("$.[1].name").value(task2.getName()));

        verify(taskService, times(1)).getTasks(project.getProjectId());
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void createTask_whenValid() throws Exception {
        Task task = new Task();
        task.setName("Task name");
        task.setCreateDateTime(LocalDateTime.now());
        String jsonTask = jacksonTask.write(task).getJson();
        task.setTaskId(4);

        when(taskService.create(any(Task.class))).thenReturn(task);

        mockMvc.perform(post(apiPath)
                .content(jsonTask)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString(apiPath + "/" + task.getTaskId())))
                .andExpect(jsonPath("$.taskId").value(task.getTaskId()))
                .andExpect(jsonPath("$.name").value(task.getName()));

        verify(taskService, times(1)).create(any(Task.class));
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void createTask_whenNotValid() throws Exception {
        Task task = new Task();
        task.setName("Task name");
        String jsonTask = jacksonTask.write(task).getJson();
        task.setTaskId(4);

        when(taskService.create(any(Task.class))).thenReturn(task);

        MvcResult mvcResult = mockMvc.perform(post(apiPath)
                .content(jsonTask)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(taskService, times(0)).create(any(Task.class));
        verifyNoMoreInteractions(taskService);

        Exception exception = mvcResult.getResolvedException();
        assertNotNull(exception);
        assertTrue(exception instanceof MethodArgumentNotValidException);
        System.out.println(exception.getMessage());
    }

    @Test
    public void updateTask() throws Exception {
        Task task = new Task();
        task.setTaskId(4);
        task.setName("Task name");

        when(taskService.update(task.getTaskId(), task)).thenReturn(task);

        mockMvc.perform(put(apiPath + "/{taskId}", task.getTaskId())
                .content(jacksonTask.write(task).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(task.getTaskId()))
                .andExpect(jsonPath("$.name").value(task.getName()));

        verify(taskService, times(1)).update(task.getTaskId(), task);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    public void deleteTask() throws Exception {
        mockMvc.perform(delete(apiPath + "/{taskId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).delete(any());
        verifyNoMoreInteractions(taskService);
    }

    @BeforeEach
    public void before(TestInfo testInfo) {
        System.out.printf("-- METODA -> %s%n", testInfo.getTestMethod().get().getName());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JacksonTester.initFields(this, mapper);
    }

    @AfterEach
    public void after(TestInfo testInfo) {
        System.out.printf("<- KONIEC -- %s%n", testInfo.getTestMethod().get().getName());
    }

}

