package com.n9.controller;

import com.n9.dto.StatusDTO;
import com.n9.exception.DebitAccountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DebitAccountExceptionController {

    @ExceptionHandler(value = DebitAccountException.class)
    public ResponseEntity handleDebitAccountException(DebitAccountException ex) {
        return new ResponseEntity<>(new StatusDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
