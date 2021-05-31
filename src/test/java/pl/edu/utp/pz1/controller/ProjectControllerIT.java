package pl.edu.utp.pz1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.edu.utp.pz1.model.Project;
import pl.edu.utp.pz1.model.Student;
import pl.edu.utp.pz1.service.ProjectService;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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
public class ProjectControllerIT {

    private final String apiPath = "/api/project";

    @MockBean
    private ProjectService mockProjectService;

    @Autowired
    private MockMvc mockMvc;

    private JacksonTester<Project> jacksonProject;
    private JacksonTester<Student> jacksonStudent;

    @Test
    public void getProjectById_whenFound() throws Exception {
        Project project = new Project();
        project.setProjectId(3);
        project.setName("Project name");

        when(mockProjectService.findById(project.getProjectId())).thenReturn(Optional.of(project));

        mockMvc.perform(get(apiPath + "/{projectId}", project.getProjectId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(project.getProjectId()))
                .andExpect(jsonPath("$.name").value(project.getName()));

        verify(mockProjectService, times(1)).findById(project.getProjectId());
        verifyNoMoreInteractions(mockProjectService);
    }

    @Test
    public void getProjectById_whenNotFound() throws Exception {
        when(mockProjectService.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(get(apiPath + "/{projectId}", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(mockProjectService, times(1)).findById(any());
        verifyNoMoreInteractions(mockProjectService);
    }

    @Test
    public void getProjects() throws Exception {
        Integer page = 5;
        Integer size = 15;

        mockMvc.perform(get(apiPath)
                .param("page", page.toString())
                .param("size", size.toString()))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(mockProjectService, times(1)).getProjects(pageableArgumentCaptor.capture());
        verifyNoMoreInteractions(mockProjectService);

        PageRequest pageable = (PageRequest) pageableArgumentCaptor.getValue();
        assertEquals(page, pageable.getPageNumber());
        assertEquals(size, pageable.getPageSize());
    }

    @Test
    public void findAll() throws Exception {
        Project project1 = new Project();
        project1.setProjectId(3);
        project1.setName("Project 1 name");

        Project project2 = new Project();
        project2.setProjectId(4);
        project2.setName("Project 2 name");

        when(mockProjectService.findAll()).thenReturn(Arrays.asList(project1, project2));

        mockMvc.perform(get(apiPath + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]").exists())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].projectId").value(project1.getProjectId()))
                .andExpect(jsonPath("$.[0].name").value(project1.getName()))
                .andExpect(jsonPath("$.[1].projectId").value(project2.getProjectId()))
                .andExpect(jsonPath("$.[1].name").value(project2.getName()));

        verify(mockProjectService, times(1)).findAll();
        verifyNoMoreInteractions(mockProjectService);
    }

    @Test
    public void findAllByUser() throws Exception {
        Project project = new Project();
        project.setProjectId(3);
        project.setName("Project name");

        Student student = new Student();
        student.setEmail("student@test.pl");

        when(mockProjectService.findAllByUser(student.getEmail())).thenReturn(Collections.singletonList(project));

        mockMvc.perform(get(apiPath + "/user/{email}", student.getEmail())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]").exists())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].projectId").value(project.getProjectId()))
                .andExpect(jsonPath("$.[0].name").value(project.getName()));

        verify(mockProjectService, times(1)).findAllByUser(student.getEmail());
        verifyNoMoreInteractions(mockProjectService);
    }

    @Test
    public void createProject_whenValid() throws Exception {
        Project project = new Project(null, "Project name", null,
                LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        String jsonProject = jacksonProject.write(project).getJson();
        project.setProjectId(3);

        when(mockProjectService.create(any(Project.class))).thenReturn(project);

        mockMvc.perform(post(apiPath)
                .content(jsonProject)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString(apiPath + "/" + project.getProjectId())))
                .andExpect(jsonPath("$.projectId").value(project.getProjectId()))
                .andExpect(jsonPath("$.name").value(project.getName()));

        verify(mockProjectService, times(1)).create(any(Project.class));
        verifyNoMoreInteractions(mockProjectService);
    }

    @Test
    public void createProject_whenNotValid() throws Exception {
        Project project = new Project(null, null, null,
                LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        String jsonProject = jacksonProject.write(project).getJson();
        project.setProjectId(3);

        when(mockProjectService.create(any(Project.class))).thenReturn(project);

        MvcResult mvcResult = mockMvc.perform(post(apiPath)
                .content(jsonProject)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(mockProjectService, times(0)).create(any(Project.class));
        verifyNoMoreInteractions(mockProjectService);

        Exception exception = mvcResult.getResolvedException();
        assertNotNull(exception);
        assertTrue(exception instanceof MethodArgumentNotValidException);
        System.out.println(exception.getMessage());
    }

    @Test
    public void updateProject() throws Exception {
        Project project = new Project();
        project.setProjectId(3);
        project.setName("Project name");

        when(mockProjectService.update(project.getProjectId(), project)).thenReturn(project);

        mockMvc.perform(put(apiPath + "/{projectId}", project.getProjectId())
                .content(jacksonProject.write(project).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(project.getProjectId()))
                .andExpect(jsonPath("$.name").value(project.getName()));

        verify(mockProjectService, times(1)).update(project.getProjectId(), project);
        verifyNoMoreInteractions(mockProjectService);
    }

    @Test
    public void addStudent() throws Exception {
        Student student = new Student();
        student.setEmail("student@test.pl");

        Project project = new Project();
        project.setProjectId(3);
        project.setName("Project name");
        project.setStudents(Collections.singleton(student));

        when(mockProjectService.addStudent(project.getProjectId(), student)).thenReturn(project);

        mockMvc.perform(put(apiPath + "/student/{projectId}", project.getProjectId())
                .content(jacksonStudent.write(student).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(project.getProjectId()))
                .andExpect(jsonPath("$.name").value(project.getName()))
                .andExpect(jsonPath("$.students[*]").exists())
                .andExpect(jsonPath("$.students.length()").value(1))
                .andExpect(jsonPath("$.students[0].email").value(student.getEmail()));

        verify(mockProjectService, times(1)).addStudent(project.getProjectId(), student);
        verifyNoMoreInteractions(mockProjectService);
    }

    @Test
    public void deleteProject() throws Exception {
        mockMvc.perform(delete(apiPath + "/{projectId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(mockProjectService, times(1)).delete(any());
        verifyNoMoreInteractions(mockProjectService);
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

