package pl.edu.utp.pz1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.edu.utp.pz1.model.Project;
import pl.edu.utp.pz1.model.Student;
import pl.edu.utp.pz1.service.StudentService;

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
public class StudentControllerIT {

    private final String apiPath = "/api/student";

    @MockBean
    private StudentService mockStudentService;

    @Autowired
    private MockMvc mockMvc;

    private JacksonTester<Project> jacksonProject;
    private JacksonTester<Student> jacksonStudent;

    @Test
    public void getStudentById_whenFound() throws Exception {
        Student student = new Student();
        student.setStudentId(3);
        student.setEmail("student@test.pl");

        when(mockStudentService.findById(student.getStudentId())).thenReturn(Optional.of(student));

        mockMvc.perform(get(apiPath + "/{studentId}", student.getStudentId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(student.getStudentId()))
                .andExpect(jsonPath("$.email").value(student.getEmail()));

        verify(mockStudentService, times(1)).findById(student.getStudentId());
        verifyNoMoreInteractions(mockStudentService);
    }

    @Test
    public void getStudentById_whenNotFound() throws Exception {
        when(mockStudentService.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(get(apiPath + "/{studentId}", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(mockStudentService, times(1)).findById(any());
        verifyNoMoreInteractions(mockStudentService);
    }

    @Test
    public void getStudentByEmail_whenFound() throws Exception {
        Student student = new Student();
        student.setStudentId(3);
        student.setEmail("student@test.pl");

        when(mockStudentService.findByEmail(student.getEmail())).thenReturn(Optional.of(student));

        mockMvc.perform(get(apiPath + "/email/{email}", student.getEmail())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(student.getStudentId()))
                .andExpect(jsonPath("$.email").value(student.getEmail()));

        verify(mockStudentService, times(1)).findByEmail(student.getEmail());
        verifyNoMoreInteractions(mockStudentService);
    }

    @Test
    public void getStudentByEmail_whenNotFound() throws Exception {
        when(mockStudentService.findByEmail(any())).thenReturn(Optional.empty());

        mockMvc.perform(get(apiPath + "/email/{email}", "1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(mockStudentService, times(1)).findByEmail(any());
        verifyNoMoreInteractions(mockStudentService);
    }

    @Test
    public void getStudents() throws Exception {
        Integer page = 5;
        Integer size = 15;

        mockMvc.perform(get(apiPath)
                .param("page", page.toString())
                .param("size", size.toString()))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(mockStudentService, times(1)).getStudents(pageableArgumentCaptor.capture());
        verifyNoMoreInteractions(mockStudentService);

        PageRequest pageable = (PageRequest) pageableArgumentCaptor.getValue();
        assertEquals(page, pageable.getPageNumber());
        assertEquals(size, pageable.getPageSize());
    }

    @Test
    public void findAll() throws Exception {
        Student student1 = new Student();
        student1.setStudentId(3);
        student1.setEmail("student1@test.pl");

        Student student2 = new Student();
        student2.setStudentId(3);
        student2.setEmail("student2@test.pl");

        when(mockStudentService.findAll()).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(get(apiPath + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]").exists())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].studentId").value(student1.getStudentId()))
                .andExpect(jsonPath("$.[0].email").value(student1.getEmail()))
                .andExpect(jsonPath("$.[1].studentId").value(student2.getStudentId()))
                .andExpect(jsonPath("$.[1].email").value(student2.getEmail()));

        verify(mockStudentService, times(1)).findAll();
        verifyNoMoreInteractions(mockStudentService);
    }

    @Test
    public void createStudent_whenValid() throws Exception {
        Student student = new Student("FirstName", "LastName", "112233",
                true, "student@test.pl", "Qwerty.1");
        String jsonStudent = jacksonStudent.write(student).getJson();
        student.setStudentId(3);

        when(mockStudentService.create(any(Student.class))).thenReturn(student);

        mockMvc.perform(post(apiPath)
                .content(jsonStudent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString(apiPath + "/" + student.getStudentId())))
                .andExpect(jsonPath("$.studentId").value(student.getStudentId()))
                .andExpect(jsonPath("$.email").value(student.getEmail()));

        verify(mockStudentService, times(1)).create(any(Student.class));
        verifyNoMoreInteractions(mockStudentService);
    }

    @Test
    public void createStudent_whenNotValid() throws Exception {
        Student student = new Student("FirstName", "LastName", "112233",
                null, "student@test.pl", "Qwerty.1");
        String jsonStudent = jacksonStudent.write(student).getJson();
        student.setStudentId(3);

        when(mockStudentService.create(any(Student.class))).thenReturn(student);

        MvcResult mvcResult = mockMvc.perform(post(apiPath)
                .content(jsonStudent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(mockStudentService, times(0)).create(any(Student.class));
        verifyNoMoreInteractions(mockStudentService);

        Exception exception = mvcResult.getResolvedException();
        assertNotNull(exception);
        assertTrue(exception instanceof MethodArgumentNotValidException);
        System.out.println(exception.getMessage());
    }

    @Test
    public void createStudent_whenConflict() throws Exception {
        Student student = new Student("FirstName", "LastName", "112233",
                true, "student@test.pl", "Qwerty.1");

        when(mockStudentService.create(any(Student.class))).thenReturn(null);

        mockMvc.perform(post(apiPath)
                .content(jacksonStudent.write(student).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isConflict());

        verify(mockStudentService, times(1)).create(any(Student.class));
        verifyNoMoreInteractions(mockStudentService);
    }

    @Test
    public void updateStudent() throws Exception {
        Student student = new Student();
        student.setStudentId(3);
        student.setEmail("student@test.pl");

        when(mockStudentService.update(student.getStudentId(), student)).thenReturn(student);

        mockMvc.perform(put(apiPath + "/{studentId}", student.getStudentId())
                .content(jacksonStudent.write(student).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(student.getStudentId()))
                .andExpect(jsonPath("$.email").value(student.getEmail()));

        verify(mockStudentService, times(1)).update(student.getStudentId(), student);
        verifyNoMoreInteractions(mockStudentService);
    }

    @Test
    public void updatePassword() throws Exception {
        Student student = new Student();
        student.setStudentId(3);
        student.setEmail("student@test.pl");

        JSONObject jsonObject = new JSONObject()
                .put("currentPassword", "password1")
                .put("newPassword", "password2");

        when(mockStudentService.updatePassword(student.getStudentId(),
                jsonObject.getString("currentPassword"),
                jsonObject.getString("newPassword"))).thenReturn(student);

        mockMvc.perform(put(apiPath + "/password/{studentId}", student.getStudentId())
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(student.getStudentId()))
                .andExpect(jsonPath("$.email").value(student.getEmail()));

        verify(mockStudentService, times(1)).updatePassword(student.getStudentId(),
                jsonObject.getString("currentPassword"),
                jsonObject.getString("newPassword"));
        verifyNoMoreInteractions(mockStudentService);
    }

    @Test
    public void deleteStudent() throws Exception {
        mockMvc.perform(delete(apiPath + "/{studentId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(mockStudentService, times(1)).delete(any());
        verifyNoMoreInteractions(mockStudentService);
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

