package pl.edu.utp.kanbanboard.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.utp.kanbanboard.model.Student;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Student student;
    private String message;
}