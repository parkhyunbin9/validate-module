package com.module.validation.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class ValidCustomException extends RuntimeException {

    private Error[] errors;

    public ValidCustomException(String message, String field) {
        this.errors = new Error[]{new Error(message, field)};
    }

    public ValidCustomException(Error[] errors) {
        this.errors = errors;
    }

    @Getter
    public static class Error {
        private String message;
        private String field;

        public Error(String message, String field) {
            this.message = message;
            this.field = field;
        }


    }
}
