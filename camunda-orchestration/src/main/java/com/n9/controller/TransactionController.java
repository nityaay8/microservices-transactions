package com.n9.controller;

import com.n9.dto.StatusDTO;
import com.n9.dto.TransactionDTO;
import com.n9.service.camunda.tx.TxConstants;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("tx")
@Slf4j
public class TransactionController {

    @Autowired
    private RuntimeService runtimeService;

    @PostMapping
    public ResponseEntity transfer(@RequestBody TransactionDTO transactionDTO) {

        ResponseEntity responseEntity = null;
        log.info("start the transfer");

        try {
            String processId = "bank_transaction";
            String businessKey = "";

            Map<String, Object> variables = new HashMap<>();
            variables.put(TxConstants.FROM_ACCOUNT_ID, transactionDTO.getFromActId());
            variables.put(TxConstants.AMOUNT, transactionDTO.getAmount());
            variables.put(TxConstants.TO_ACCOUNT_ID, transactionDTO.getToActId());

            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("bank_transaction", variables);
            responseEntity = ResponseEntity.ok(new StatusDTO("process started, id = " + processInstance.getProcessInstanceId()));
            log.info(" transfer success");
        } catch (Exception e) {
            responseEntity = ResponseEntity.ok(new StatusDTO(e.getMessage()));
            log.info(" transfer failed", e);
        } finally {
            log.info("end the transfer");
        }

        return responseEntity;

    }
}
