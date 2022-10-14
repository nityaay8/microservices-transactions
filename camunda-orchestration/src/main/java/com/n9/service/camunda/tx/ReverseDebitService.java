package com.n9.service.camunda.tx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n9.dto.DebitDTO;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Slf4j
public class ReverseDebitService extends DebitService {


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("reverse debit  process started");
        Long accountId = (Long) execution.getVariable(TxConstants.FROM_ACCOUNT_ID);

        Float amount = (Float) execution.getVariable(TxConstants.AMOUNT);

        performDebit(execution, accountId, amount * -1);

        log.info("reverse debit  process ended");

    }
}
