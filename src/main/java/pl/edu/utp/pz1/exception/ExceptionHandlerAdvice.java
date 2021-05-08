package pl.edu.utp.pz1.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity handleEmailUsedException(ProjectNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.notFound().build();
    }
}