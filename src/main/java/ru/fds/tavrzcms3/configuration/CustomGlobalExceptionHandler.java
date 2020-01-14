package ru.fds.tavrzcms3.configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> "field: " + x.getField()+ ". message: " + x.getDefaultMessage() + ". wrong value: " + x.getRejectedValue())
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleBadValidation(ConstraintViolationException ex) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Ошибка валидации: ");
        ex.getConstraintViolations().forEach(c ->
                stringBuilder.append(c.getMessage())
                        .append("(")
                        .append(c.getInvalidValue())
                        .append(") в поле: \"")
                        .append(c.getPropertyPath())
                        .append("\". "));

        return new ResponseEntity<>(stringBuilder.toString(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
