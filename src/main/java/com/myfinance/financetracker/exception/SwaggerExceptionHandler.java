package com.myfinance.financetracker.exception;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@ControllerAdvice
@Order(1) // Более высокий приоритет
public class SwaggerExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> handleSwaggerException(Exception ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("org.springdoc")) {
            return new ResponseEntity<>("Swagger error ignored", HttpStatus.OK);
        }
        return null; // Пропускаем обработку, если ошибка не связана со Swagger
    }
}