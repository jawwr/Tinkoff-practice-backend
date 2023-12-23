package com.project.tinkoff.rest;

import com.project.tinkoff.exception.DataNotFoundException;
import com.project.tinkoff.exception.PermissionDeniedException;
import com.project.tinkoff.exception.UserAlreadyExistInProjectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler({
            DataNotFoundException.class,
            PermissionDeniedException.class,
            UserAlreadyExistInProjectException.class
    })
    public ResponseEntity<ErrorMessageResponse> dataNotFoundException(DataNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> otherExceptionHandler(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorMessageResponse(e.getMessage()));
    }
}
