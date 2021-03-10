package pl.edu.utp.pz1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "projects")
@NoArgsConstructor
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 1000)
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateDateTime;

    private LocalDateTime submitDateTime;

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties({"project"})
    private List<Task> tasks;

    @ManyToMany
    @JoinTable(name = "project_student",
            joinColumns = {@JoinColumn(name = "projectId")},
            inverseJoinColumns = {@JoinColumn(name = "studentId")})
    @JsonIgnoreProperties({"project"})
    private Set<Student> students;

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
