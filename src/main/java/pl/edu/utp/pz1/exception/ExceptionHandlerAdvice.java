package pl.edu.utp.pz1.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity handleProjectNotFound(ProjectNotFoundException ex) {
        return handleError(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity handleTaskNotFound(TaskNotFoundException ex) {
        return handleError(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity handleStudentNotFound(StudentNotFoundException ex) {
        return handleError(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity handleEmailAlreadyUsed(EmailAlreadyUsedException ex) {
        return handleError(ex, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity handleError(RuntimeException ex, HttpStatus status) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(status).body(ex);
    }
}