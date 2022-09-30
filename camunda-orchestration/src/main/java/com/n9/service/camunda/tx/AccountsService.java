package com.n9.service.camunda.tx;


import com.n9.dto.AccountDTO;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

public class AccountsService implements JavaDelegate {

    private final static Logger LOGGER = Logger.getLogger(AccountsService.class.getName());

    @Value("${account_url}")
    private String accountsUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("validation start");
        LOGGER.info("performing the account validation");
        Long accountId = (Long) execution.getVariable(TxConstants.FROM_ACCOUNT_ID);
        Float amount = (Float) execution.getVariable(TxConstants.AMOUNT);

        ResponseEntity<AccountDTO> accountDTOResponseEntity = restTemplate.getForEntity(accountsUrl + "/" + accountId, AccountDTO.class);

        LOGGER.info("accountDTOResponseEntity = " + accountDTOResponseEntity);

        if (accountDTOResponseEntity.getStatusCode().is2xxSuccessful() && accountDTOResponseEntity.getBody() != null) {
            if (amount > accountDTOResponseEntity.getBody().getAmount()) {
                execution.setVariable(TxConstants.VALID_ACT, false);
                LOGGER.info("in sufficient balance");
            } else {
                execution.setVariable(TxConstants.VALID_ACT, true);
            }
        } else {
            execution.setVariable(TxConstants.VALID_ACT, false);
        }
        LOGGER.info("validation end");
    }
}
