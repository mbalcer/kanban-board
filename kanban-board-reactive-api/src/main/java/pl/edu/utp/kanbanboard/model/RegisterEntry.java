package pl.edu.utp.kanbanboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Map;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterEntry {

    @Id
    private String entryId;

    private LocalDate date;

    private Map<TaskState, Integer> flow;

    private String project;

}
