package pl.edu.utp.pz1.exception;

public class EmailAlreadyUsedException extends RuntimeException {

    private static final String MESSAGE = "Email is already used!";

    public EmailAlreadyUsedException() {
        super(MESSAGE);
    }
}