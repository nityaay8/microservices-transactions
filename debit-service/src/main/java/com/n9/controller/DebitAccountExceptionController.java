package com.n9.controller;

import com.n9.dto.StatusDTO;
import com.n9.exception.DebitAccountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class DebitAccountExceptionController {

    @ExceptionHandler(value = DebitAccountException.class)
    public ResponseEntity handleDebitAccountException(DebitAccountException ex) {
        log.error("debit exception ", ex);
        return new ResponseEntity<>(new StatusDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handleException(Exception ex) {
        log.error("generic debit exception ", ex);
        return new ResponseEntity<>(new StatusDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
