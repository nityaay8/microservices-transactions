package com.n9.service.camunda.tx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n9.dto.CreditDTO;
import com.n9.filter.MdcFilter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Slf4j
public class CreditService implements JavaDelegate {


    @Value("${credit_api_url}")
    private String creditUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info(" credit process started");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(MdcFilter.CORRELATION_ID, MDC.get(MdcFilter.CORRELATION_ID));

        Long accountId = (Long) execution.getVariable(TxConstants.TO_ACCOUNT_ID);

        Float amount = (Float) execution.getVariable(TxConstants.AMOUNT);

        CreditDTO creditDTO = new CreditDTO();
        creditDTO.setActId(accountId);
        creditDTO.setAmount(amount);

        HttpEntity<CreditDTO> request =
                new HttpEntity<CreditDTO>(creditDTO, headers);

        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(creditUrl, request, String.class);
        log.info("responseEntity = " + responseEntity);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("credit transaction success  ");
            execution.setVariable(TxConstants.SUCCESS, true);
        } else {
            log.info("credit transaction failed  ");
            execution.setVariable(TxConstants.SUCCESS, false);
        }

        log.info(" credit process ended");
    }
}
