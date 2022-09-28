package com.n9.controller;

import com.n9.dto.DebitDTO;
import com.n9.dto.StatusDTO;
import com.n9.exception.DebitAccountException;
import com.n9.service.DebitDataService;
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
public class DebitController {

    Logger logger = LoggerFactory.getLogger(DebitController.class);

    public DebitController(){

    }

    @Autowired
    private DebitDataService debitDataService;

    @PostMapping
    public ResponseEntity performDebit(@RequestBody DebitDTO debitDTO) {

        ResponseEntity responseEntity = null;


        try {
            if (debitDTO.getActId() == null || debitDTO.getAmount() == null) {
                logger.warn("invalid account details = " + debitDTO);
                responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusDTO("invalid account details"));
            } else {
                debitDTO = debitDataService.performDebit(debitDTO);
                responseEntity = ResponseEntity.ok(debitDTO);
            }
        } catch (DebitAccountException de) {
            throw de;
        } catch (Exception e) {
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StatusDTO(e.getMessage()));
        }


        return responseEntity;

    }
}
