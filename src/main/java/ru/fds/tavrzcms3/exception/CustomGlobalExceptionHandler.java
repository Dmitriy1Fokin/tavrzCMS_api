package ru.fds.tavrzcms3.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String FIELD = "field";
    private static final String MESSAGE = "message";
    private static final String WRONG_VALUE = "wrong value";
    private static final String ERRORS = "errors";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, status.value());

        List<Map<String, Object>> errors = new ArrayList<>();
        for(FieldError fieldError : ex.getBindingResult().getFieldErrors()){
            Map<String, Object> map = new HashMap<>();
            map.put(FIELD, fieldError.getField());
            map.put(MESSAGE, fieldError.getDefaultMessage());
            map.put(WRONG_VALUE, fieldError.getRejectedValue());
            errors.add(map);
        }

        body.put(ERRORS, errors);
        body.put(MESSAGE, "validation error");

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());

        List<Map<String, Object>> errors = new ArrayList<>();
        for(ConstraintViolation violation : ex.getConstraintViolations()){
            Map<String, Object> map = new HashMap<>();
            map.put(FIELD, violation.getPropertyPath().toString());
            map.put(MESSAGE, violation.getMessage());
            map.put(WRONG_VALUE, violation.getInvalidValue());
            errors.add(map);
        }

        body.put(ERRORS, errors);
        body.put(MESSAGE, "validation error");

        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<Object> handleNullPointerException(NullPointerException ex){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(MESSAGE, ex.getMessage());
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }



}
