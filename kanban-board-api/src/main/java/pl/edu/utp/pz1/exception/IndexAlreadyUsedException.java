package pl.edu.utp.pz1.exception;

public class IndexAlreadyUsedException extends RuntimeException {

    private static final String MESSAGE = "Index number is already used.";

    public IndexAlreadyUsedException() {
        super(MESSAGE);
    }

}
