package pl.edu.utp.kanbanboard.util;

import pl.edu.utp.kanbanboard.model.Task;
import pl.edu.utp.kanbanboard.model.TaskState;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaskTestUntil {

    public static Task getEmptyTask() {
        return new Task();
    }

    public static Task generateTask1() {
        return new Task(UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "123",
                1,
                TaskState.DONE,
                LocalDateTime.now(),
                "123",
                "123");
    }

    public static Task generateTask2() {
        return new Task(UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "456",
                2,
                TaskState.DONE,
                LocalDateTime.now(),
                "123",
                "456");
    }

    public static Task generateTask3() {
        return new Task(UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "789",
                3,
                TaskState.DONE,
                LocalDateTime.now(),
                "222",
                "789");
    }
}