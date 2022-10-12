package com.n9.controller;

import com.n9.dto.AccountDTO;
import com.n9.dto.StatusDTO;
import com.n9.entity.Account;
import com.n9.service.CreditDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("creditAccount")
@Slf4j
public class CreditAccountController {

    @Autowired
    private CreditDataService creditDataService;

    @PostMapping
    public ResponseEntity save(@RequestBody AccountDTO accountDTO) {

        ResponseEntity responseEntity = null;
        log.debug("save enter");

        if (accountDTO.getAmount() == null || accountDTO.getName() == null) {

            log.warn("invalid account details {} ", accountDTO);
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusDTO("invalid account details"));
        } else {

            AccountDTO savedAccountDTO = creditDataService.saveAccount(accountDTO);
            log.info("account created successfully {} ", accountDTO);
            responseEntity = ResponseEntity.ok(savedAccountDTO);
        }
        log.debug("save exit");
        return responseEntity;
    }
}
