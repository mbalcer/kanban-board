package pl.edu.utp.kanbanboard.event;

import org.springframework.context.ApplicationEvent;
import pl.edu.utp.kanbanboard.model.Student;

public class StudentCreatedEvent extends ApplicationEvent {
    public StudentCreatedEvent(Student source) {
        super(source);
    }
}
