package pl.edu.utp.pz1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskId;

    @Column(nullable = false, length = 50)
    @NotNull
    private String name;

    @Column(length = 1000)
    private String description;

    private Integer sequence;

    @Enumerated(EnumType.STRING)
    private TaskState state = TaskState.TODO;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createDateTime;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "projectId")
    @JsonIgnoreProperties({"tasks"})
    private Project project;

    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;

    public Task(String name, String description, Integer sequence) {
        this.name = name;
        this.description = description;
        this.sequence = sequence;
    }

}
