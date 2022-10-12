package com.n9.service.camunda.tx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n9.dto.DebitDTO;
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
public class DebitService implements JavaDelegate {


    @Value("${debit_api_url}")
    private String debitUrl;


    @Autowired
    private RestTemplate restTemplate;

    public void performDebit(DelegateExecution execution, Long accountId, Float amount) {
        log.info(" debit process begin");
        DebitDTO debitDTO = new DebitDTO();
        debitDTO.setActId(accountId);
        debitDTO.setAmount(amount);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(MdcFilter.CORRELATION_ID, MDC.get(MdcFilter.CORRELATION_ID));

        HttpEntity<DebitDTO> request =
                new HttpEntity<DebitDTO>(debitDTO, headers);

        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(debitUrl, request, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info(" debit process success");
            execution.setVariable(TxConstants.SUCCESS, true);
        } else {
            log.warn(" debit process failed");
            execution.setVariable(TxConstants.SUCCESS, false);
        }
        log.info(" debit process completed");
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info(" debit process started");
        Long accountId = (Long) execution.getVariable(TxConstants.FROM_ACCOUNT_ID);

        Float amount = (Float) execution.getVariable(TxConstants.AMOUNT);

        performDebit(execution, accountId, amount);


        log.info(" debit process ended");

    }
}
