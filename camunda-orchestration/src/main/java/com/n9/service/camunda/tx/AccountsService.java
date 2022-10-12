package com.n9.service.camunda.tx;


import com.n9.dto.AccountDTO;
import com.n9.dto.DebitDTO;
import com.n9.filter.MdcFilter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Slf4j
public class AccountsService implements JavaDelegate {


    @Value("${account_api_url}")
    private String accountsUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.debug("validation process start");
        log.info("performing the account validation process");
        Long accountId = (Long) execution.getVariable(TxConstants.FROM_ACCOUNT_ID);
        Float amount = (Float) execution.getVariable(TxConstants.AMOUNT);

        HttpHeaders headers = new HttpHeaders();
        headers.set(MdcFilter.CORRELATION_ID, MDC.get(MdcFilter.CORRELATION_ID));

        HttpEntity<DebitDTO> request =
                new HttpEntity<DebitDTO>(null, headers);

        ResponseEntity<AccountDTO> accountDTOResponseEntity = restTemplate.exchange(accountsUrl + "/" + accountId, HttpMethod.GET, request, AccountDTO.class);

        log.info("accountDTOResponseEntity = " + accountDTOResponseEntity);

        if (accountDTOResponseEntity.getStatusCode().is2xxSuccessful() && accountDTOResponseEntity.getBody() != null) {
            if (amount > accountDTOResponseEntity.getBody().getAmount()) {
                execution.setVariable(TxConstants.VALID_ACT, false);
                log.info("in sufficient balance");
            } else {
                execution.setVariable(TxConstants.VALID_ACT, true);
            }
        } else {
            execution.setVariable(TxConstants.VALID_ACT, false);
        }
        log.info("account validation process end");
    }
}
