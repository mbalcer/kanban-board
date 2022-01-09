package pl.edu.utp.kanbanboard.event;

import org.springframework.context.ApplicationEvent;
import pl.edu.utp.kanbanboard.model.Task;

public class TaskEditedEvent extends ApplicationEvent {
    public TaskEditedEvent(Task source) {
        super(source);
    }
}
