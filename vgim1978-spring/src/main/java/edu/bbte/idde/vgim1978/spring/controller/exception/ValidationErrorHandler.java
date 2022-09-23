package edu.bbte.idde.vgim1978.spring.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.stream.Stream;

@Slf4j
@ControllerAdvice
public class ValidationErrorHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Stream<String> handleConstraintViolation(ConstraintViolationException e) {
        log.error("Constraint violated", e);
        return e.getConstraintViolations().stream().map(constraintViolation ->
                constraintViolation.getPropertyPath().toString()
                + " " + constraintViolation.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Stream<String> methodArgumentNotValid(MethodArgumentNotValidException e) {
        log.error("Method argument violated", e);
        return e.getFieldErrors().stream().map(fieldError ->
                fieldError.getField()
                        + " " + fieldError.getDefaultMessage());
    }

    @ExceptionHandler(MySqlException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String mySqlException(MySqlException e) {
        return e.getMessage();
    }
}
