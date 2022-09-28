package com.n9.controller;

import com.n9.dto.AccountDTO;
import com.n9.dto.DebitDTO;
import com.n9.dto.StatusDTO;
import com.n9.exception.AccountException;
import com.n9.service.AccountDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    public AccountController() {

    }

    @Autowired
    private AccountDataService accountDataService;

    @GetMapping
    public ResponseEntity getAllAccounts() {
        ResponseEntity responseEntity;
        List<AccountDTO> accountDTOList = accountDataService.getAllAccount();
        responseEntity = ResponseEntity.ok(accountDTOList);
        return responseEntity;
    }

    @GetMapping("{accountId}")
    public ResponseEntity getAccount(@PathVariable("accountId") Long accountId) {
        ResponseEntity responseEntity;
        AccountDTO accountDTO = accountDataService.getAccount(accountId);
        responseEntity = ResponseEntity.ok(accountDTO);
        return responseEntity;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody AccountDTO accountDTO) {

        ResponseEntity responseEntity = null;

        if (accountDTO.getAmount() == null) {
            logger.warn("invalid account details = " + accountDTO);
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusDTO("invalid account details"));
        } else {
            accountDTO = accountDataService.save(accountDTO);
            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
        }
        return responseEntity;

    }

    @PutMapping
    public ResponseEntity update(@RequestBody AccountDTO accountDTO) {

        ResponseEntity responseEntity = null;

        if (accountDTO.getAccountId() == null || accountDTO.getAmount() == null) {
            logger.warn("invalid account details = " + accountDTO);
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusDTO("invalid account details"));
        } else {
            accountDTO = accountDataService.update(accountDTO);
            responseEntity = ResponseEntity.ok(accountDTO);
        }

        return responseEntity;

    }

    @DeleteMapping("{accountId}")
    public ResponseEntity removeAccount(@PathVariable("accountId") Long accountId) {
        ResponseEntity responseEntity;
        accountDataService.removeAccount(accountId);
        responseEntity = ResponseEntity.ok(HttpStatus.NO_CONTENT);
        return responseEntity;
    }
}
