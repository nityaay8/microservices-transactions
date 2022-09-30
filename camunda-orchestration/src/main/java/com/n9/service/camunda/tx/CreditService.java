package com.n9.service.camunda.tx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n9.dto.CreditDTO;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

public class CreditService implements JavaDelegate {

    private final static Logger LOGGER = Logger.getLogger(CreditService.class.getName());

    @Value("${credit_url}")
    private String creditUrl ;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("credit start");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Long accountId = (Long) execution.getVariable(TxConstants.TO_ACCOUNT_ID);

        Float amount = (Float) execution.getVariable(TxConstants.AMOUNT);

        CreditDTO creditDTO = new CreditDTO();
        creditDTO.setActId(accountId);
        creditDTO.setAmount(amount);

        HttpEntity<CreditDTO> request =
                new HttpEntity<CreditDTO>(creditDTO, headers);

        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(creditUrl, request, String.class);


        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            execution.setVariable(TxConstants.SUCCESS, true);
        } else {
            LOGGER.info("credit transaction failed  ");
            LOGGER.info("responseEntity = " + responseEntity);
            execution.setVariable(TxConstants.SUCCESS, false);
        }

        LOGGER.info("credit end");
    }
}
