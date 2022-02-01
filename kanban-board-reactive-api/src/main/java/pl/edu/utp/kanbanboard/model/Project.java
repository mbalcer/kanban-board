package pl.edu.utp.kanbanboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Project {

    @Id
    private String projectId;

    private String name;

    private String description;

    private LocalDateTime createDateTime;

    private LocalDateTime updateDateTime;

    private LocalDateTime submitDateTime;

    private Set<String> tasks = new HashSet<>();

    private Set<String> students = new HashSet<>();

    public Project(String projectId, String name, String description) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
    }
}
