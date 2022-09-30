package com.n9.service.camunda.tx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n9.dto.DebitDTO;
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

public class DebitService implements JavaDelegate {

    private final static Logger LOGGER = Logger.getLogger(DebitService.class.getName());

    @Value("${debit_url}")
    private String debitUrl;


    @Autowired
    private RestTemplate restTemplate;

    public void performDebit(DelegateExecution execution, Long accountId, Float amount) {
        DebitDTO debitDTO = new DebitDTO();
        debitDTO.setActId(accountId);
        debitDTO.setAmount(amount);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<DebitDTO> request =
                new HttpEntity<DebitDTO>(debitDTO, headers);

        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(debitUrl, request, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            execution.setVariable(TxConstants.SUCCESS, true);
        } else {
            execution.setVariable(TxConstants.SUCCESS, false);
        }
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("debit start");
        Long accountId = (Long) execution.getVariable(TxConstants.FROM_ACCOUNT_ID);

        Float amount = (Float) execution.getVariable(TxConstants.AMOUNT);

        performDebit(execution, accountId, amount);

        LOGGER.info("debit end");

    }
}
