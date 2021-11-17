package pl.edu.utp.pz1.exception;

public class ProjectNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Project with given ID not found";

    public ProjectNotFoundException() {
        super(MESSAGE);
    }
}
