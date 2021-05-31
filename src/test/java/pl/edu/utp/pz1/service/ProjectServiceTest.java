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
import pl.edu.utp.pz1.exception.ProjectNotFoundException;
import pl.edu.utp.pz1.model.Project;
import pl.edu.utp.pz1.model.Student;
import pl.edu.utp.pz1.model.Task;
import pl.edu.utp.pz1.repository.ProjectRepository;
import pl.edu.utp.pz1.repository.TaskRepository;
import pl.edu.utp.pz1.service.impl.ProjectServiceImpl;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @Test
    public void findById() {
        Project given = new Project();
        given.setProjectId(1);
        given.setName("Project name");

        when(projectRepository.findById(given.getProjectId())).thenReturn(Optional.of(given));
        Project actual = projectService.findById(given.getProjectId()).orElse(null);

        assertNotNull(actual);
        assertEquals(given.getName(), actual.getName());
        verify(projectRepository, times(1)).findById(given.getProjectId());
        verifyNoMoreInteractions(projectRepository);
    }

    @Test
    public void findAll() {
        Project project1 = new Project();
        project1.setProjectId(1);
        project1.setName("Project 1 name");

        Project project2 = new Project();
        project2.setProjectId(2);
        project2.setName("Project 2 name");

        List<Project> given = Arrays.asList(project1, project2);

        when(projectRepository.findAll()).thenReturn(given);
        List<Project> actual = projectService.findAll();

        assertEquals(given.size(), actual.size());
        assertThat(given, containsInAnyOrder(project1, project2));
        verify(projectRepository, times(1)).findAll();
        verifyNoMoreInteractions(projectRepository);
    }

    @Test
    public void findAllByUser() {
        Project project = new Project();
        project.setProjectId(1);
        project.setName("Project name");

        List<Project> given = Collections.singletonList(project);

        when(projectRepository.findAllByStudentsEmail(any())).thenReturn(given);
        List<Project> actual = projectService.findAllByUser(any());

        assertEquals(given.size(), actual.size());
        assertThat(actual, containsInAnyOrder(project));
        verify(projectRepository, times(1)).findAllByStudentsEmail(any());
        verifyNoMoreInteractions(projectRepository);
    }

    @Test
    public void getProjects() {
        int page = 5;
        int size = 15;
        Pageable pageable = PageRequest.of(page, size);

        when(projectRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));
        Page<Project> projectPage = projectService.getProjects(pageable);

        assertEquals(page, projectPage.getPageable().getPageNumber());
        assertEquals(size, projectPage.getPageable().getPageSize());
        verify(projectRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(projectRepository);
    }

    @Test
    public void createProject() {
        Project given = new Project(3, "Project name", null,
                LocalDateTime.now(), LocalDateTime.now(), null, null, null);

        when(projectRepository.save(given)).thenReturn(given);
        Project actual = projectService.create(given);

        assertEquals(given.getName(), actual.getName());
        verify(projectRepository, times(1)).save(given);
        verifyNoMoreInteractions(projectRepository);
    }

    @Test
    public void updateProject_whenFound() {
        Project given = new Project();
        given.setProjectId(1);
        given.setName(null);
        given.setDescription(null);
        given.setSubmitDateTime(null);

        Project newProject = new Project();
        given.setProjectId(3);
        newProject.setName("Project name");
        newProject.setDescription("Description");
        newProject.setSubmitDateTime(LocalDateTime.now());

        when(projectRepository.findById(given.getProjectId())).thenReturn(Optional.of(given));
        when(projectRepository.save(given)).thenReturn(given);
        Project actual = projectService.update(given.getProjectId(), newProject);

        assertEquals(given.getProjectId(), actual.getProjectId());
        assertEquals(newProject.getName(), actual.getName());
        assertEquals(newProject.getDescription(), actual.getDescription());
        assertEquals(newProject.getSubmitDateTime(), actual.getSubmitDateTime());
        verify(projectRepository, times(1)).findById(given.getProjectId());
        verify(projectRepository, times(1)).save(given);
        verifyNoMoreInteractions(projectRepository);
    }

    @Test
    public void updateProject_whenNotFound() {
        Project given = new Project();
        given.setProjectId(1);
        given.setName(null);
        given.setDescription(null);
        given.setSubmitDateTime(null);

        Project newProject = new Project();
        given.setProjectId(3);
        newProject.setName("Project name");
        newProject.setDescription("Description");
        newProject.setSubmitDateTime(LocalDateTime.now());

        when(projectRepository.findById(given.getProjectId())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.update(given.getProjectId(), newProject));
        assertNotEquals(given.getProjectId(), newProject.getProjectId());
        assertNotEquals(given.getName(), newProject.getName());
        assertNotEquals(given.getDescription(), newProject.getDescription());
        assertNotEquals(given.getSubmitDateTime(), newProject.getSubmitDateTime());
        verify(projectRepository, times(1)).findById(any());
        verify(projectRepository, times(0)).save(any());
        verifyNoMoreInteractions(projectRepository);
    }

    @Test
    public void addStudent_whenProjectFound() {
        Student student = new Student();
        student.setStudentId(3);

        Project given = new Project();
        given.setProjectId(1);
        given.setName("Project name");

        when(projectRepository.findById(given.getProjectId())).thenReturn(Optional.of(given));
        when(projectRepository.save(given)).thenReturn(given);
        Project actual = projectService.addStudent(given.getProjectId(), student);

        assertEquals(given.getName(), actual.getName());
        assertThat(actual.getStudents(), containsInAnyOrder(student));
        verify(projectRepository, times(1)).findById(given.getProjectId());
        verify(projectRepository, times(1)).save(given);
        verifyNoMoreInteractions(projectRepository);
    }

    @Test
    public void addStudent_whenProjectNotFound() {
        Student student = new Student();
        student.setStudentId(3);

        Project given = new Project();
        given.setProjectId(1);
        given.setName("Project name");

        when(projectRepository.findById(given.getProjectId())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.addStudent(given.getProjectId(), student));
        assertThat(given.getStudents(), not(containsInAnyOrder(student)));
        verify(projectRepository, times(1)).findById(given.getProjectId());
        verify(projectRepository, times(0)).save(given);
        verifyNoMoreInteractions(projectRepository);
    }

    @Test
    public void deleteProject_whenFound() {
        Project given = new Project();
        given.setProjectId(1);
        given.setTasks(Arrays.asList(new Task(), new Task()));

        when(projectRepository.findById(given.getProjectId())).thenReturn(Optional.of(given));
        doNothing().when(projectRepository).delete(any(Project.class));
        doNothing().when(taskRepository).delete(any(Task.class));

        projectService.delete(given.getProjectId());

        verify(projectRepository, times(1)).findById(given.getProjectId());
        verify(projectRepository, times(1)).delete(given);
        verify(taskRepository, times(given.getTasks().size())).delete(any(Task.class));
        verifyNoMoreInteractions(projectRepository);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    public void deleteProject_whenNotFound() {
        when(projectRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.delete(any()));
        verify(projectRepository, times(1)).findById(any());
        verify(projectRepository, times(0)).delete(any(Project.class));
        verify(taskRepository, times(0)).delete(any(Task.class));
        verifyNoMoreInteractions(projectRepository);
        verifyNoMoreInteractions(taskRepository);
    }

}
