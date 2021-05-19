package pl.edu.utp.pz1.exception;

public class StudentNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Student with given ID not found";

    public StudentNotFoundException() {
        super(MESSAGE);
    }
}
