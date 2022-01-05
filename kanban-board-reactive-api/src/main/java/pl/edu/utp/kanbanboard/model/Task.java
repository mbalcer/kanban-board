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

    private Integer order;

    private LocalDateTime deliveryDateTime;
}
