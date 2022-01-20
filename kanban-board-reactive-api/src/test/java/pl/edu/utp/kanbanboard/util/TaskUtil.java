package pl.edu.utp.kanbanboard.util;

import pl.edu.utp.kanbanboard.model.Task;
import pl.edu.utp.kanbanboard.model.TaskState;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaskUtil {
    public static Task generateTask() {
        return new Task(UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "123",
                1,
                TaskState.DONE,
                LocalDateTime.now(),
                "123",
                "123");
    }
}
