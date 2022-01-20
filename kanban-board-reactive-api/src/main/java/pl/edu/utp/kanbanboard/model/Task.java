package pl.edu.utp.kanbanboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task {
    @Id
    private String taskId;

    private String name;

    private String description;

    private Integer sequence;

    private TaskState state = TaskState.TODO;

    private LocalDateTime createDateTime;

    private String projectId;

    private String studentId;

    public Task(String name, String description, Integer sequence) {
        this.name = name;
        this.description = description;
        this.sequence = sequence;
    }

    public Task(String taskId, String name, String description, Integer sequence, TaskState state, LocalDateTime createDateTime) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.sequence = sequence;
        this.state = state;
        this.createDateTime = createDateTime;
    }
}
