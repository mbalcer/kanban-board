package pl.edu.utp.pz1.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.utp.pz1.model.Student;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Student student;
    private String message;
}
