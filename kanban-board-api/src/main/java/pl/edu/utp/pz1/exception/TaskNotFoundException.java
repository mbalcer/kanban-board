package pl.edu.utp.pz1.exception;

public class TaskNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Task with given ID not found";

    public TaskNotFoundException() {
        super(MESSAGE);
    }
}
