package com.manager.payments.adapter.in.rest.exception;

import com.manager.payments.model.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(PlayerAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(PlayerAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<String> handlePaymentNotFoundException(PaymentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(PlayerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ReceiptNotFoundException.class)
    public ResponseEntity<String> handleReceiptNotFoundException(ReceiptNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(PlayerNotAssignedException.class)
    public ResponseEntity<String> handleUserNotAssignedException(PlayerNotAssignedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(PaymentNotAssignedException.class)
    public ResponseEntity<String> handlePaymentNotAssignedException(PaymentNotAssignedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(PaymentInvalidDateInterval.class)
    public ResponseEntity<String> handlePaymentInvalidDateInterval(PaymentInvalidDateInterval e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(PaymentAlreadyExpired.class)
    public ResponseEntity<String> handlePaymentAlreadyExpired(PaymentAlreadyExpired e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(PlayerPaymentAssignmentInconsistent.class)
    public ResponseEntity<String> handlePlayerPaymentAssignmentInconsistent(PlayerPaymentAssignmentInconsistent e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
