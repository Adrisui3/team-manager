package com.manager.shared.exception;

import com.manager.shared.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto<String>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()));
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ResponseDto<String>> handleGenericException(GenericException e) {
        HttpStatus httpStatus = switch (e.getStatus()) {
            case OK -> HttpStatus.OK;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_STATE -> HttpStatus.BAD_REQUEST;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
        };

        return ResponseEntity.status(httpStatus).body(new ResponseDto<>(httpStatus.value(), e.getMessage()));
    }
}
