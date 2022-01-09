package pl.edu.utp.kanbanboard.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.edu.utp.kanbanboard.model.Task;

@Getter
public class TaskEditedEvent extends ApplicationEvent {
    private String action;

    public TaskEditedEvent(Task source, String action) {
        super(source);
        this.action = action;
    }
}
