package pl.edu.utp.pz1.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.utp.pz1.model.Project;
import pl.edu.utp.pz1.model.Student;
import pl.edu.utp.pz1.service.ProjectService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/project")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Integer projectId) {
        return ResponseEntity.of(projectService.findById(projectId));
    }

    @GetMapping
    public Page<Project> getProjects(@RequestParam("page") int page, @RequestParam("size") int size) {
        return projectService.getProjects(PageRequest.of(page, size));
    }

    @GetMapping("/all")
    public List<Project> findAll() {
        return projectService.findAll();
    }

    @GetMapping("/user/{email}")
    public List<Project> findAllByUser(@PathVariable String email) {
        return projectService.findAllByUser(email);
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) {
        Project newProject = projectService.create(project);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/" + newProject.getProjectId()).build().toUri();
        return ResponseEntity.created(location).body(newProject);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable Integer projectId, @RequestBody Project project) {
        Project updatedProject = projectService.update(projectId, project);

        return ResponseEntity.ok(updatedProject);
    }

    @PutMapping("/student/{projectId}")
    public ResponseEntity<Project> addStudent(@PathVariable Integer projectId, @RequestBody Student student) {
        Project updatedProject = projectService.addStudent(projectId, student);

        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer projectId) {
        projectService.delete(projectId);
        return ResponseEntity.noContent().build();
    }
}
