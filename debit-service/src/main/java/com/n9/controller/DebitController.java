package com.n9.controller;

import com.n9.dto.DebitDTO;
import com.n9.dto.StatusDTO;
import com.n9.exception.DebitAccountException;
import com.n9.service.DebitDataService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debit")
@Slf4j
public class DebitController {


    public DebitController() {

    }

    @Autowired
    private DebitDataService debitDataService;

    @PostMapping
    public ResponseEntity performDebit(@RequestBody DebitDTO debitDTO) {

        log.debug("perform debit start");
        ResponseEntity responseEntity = null;

        if (debitDTO.getActId() == null || debitDTO.getAmount() == null) {
            log.warn("invalid account details {} ", debitDTO);
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusDTO("invalid account details"));
        } else {
            debitDTO = debitDataService.performDebit(debitDTO);
            responseEntity = ResponseEntity.ok(debitDTO);
        }

        log.debug("perform debit end");
        return responseEntity;

    }
}
