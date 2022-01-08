package pl.edu.utp.kanbanboard.event;

import org.springframework.context.ApplicationEvent;
import pl.edu.utp.kanbanboard.model.Task;

public class TaskCreatedEvent extends ApplicationEvent {
    public TaskCreatedEvent(Task source) {
        super(source);
    }
}
