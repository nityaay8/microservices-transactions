package com.n9.controller;

import com.n9.dto.CreditDTO;
import com.n9.dto.StatusDTO;
import com.n9.exception.CreditAccountException;
import com.n9.service.CreditDataService;
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
@RequestMapping("/credit")
public class CreditController {

    Logger logger = LoggerFactory.getLogger(CreditController.class);

    public CreditController() {

    }

    @Autowired
    private CreditDataService debitDataService;

    @PostMapping
    public ResponseEntity performCredit(@RequestBody CreditDTO debitDTO) {

        ResponseEntity responseEntity = null;


        if (debitDTO.getActId() == null || debitDTO.getAmount() == null) {
            logger.warn("invalid account details = " + debitDTO);
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusDTO("invalid account details"));
        } else {
            debitDTO = debitDataService.performCredit(debitDTO);
            responseEntity = ResponseEntity.ok(debitDTO);
        }


        return responseEntity;

    }
}
