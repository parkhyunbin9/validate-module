package com.module.validation.domain.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String inValidArgs(Exception e) {
        System.out.println(e.getMessage());
        System.out.println(e.getCause());
        return "invalid";
    }
}
