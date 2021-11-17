package pl.edu.utp.pz1.exception;

public class PasswordNotCorrectException extends RuntimeException {

    private static final String MESSAGE = "Password is not correct!";

    public PasswordNotCorrectException() {
        super(MESSAGE);
    }
}