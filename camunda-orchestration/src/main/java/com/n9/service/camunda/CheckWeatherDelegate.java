package com.n9.service.camunda;

import com.n9.dto.AccountDTO;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class CheckWeatherDelegate implements JavaDelegate {

    private Random random = new Random();

    private final static Logger LOGGER = Logger.getLogger(PrintWeatherDetails.class.getName());

//    @Autowired
//    private AccountDataService accountDataService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        delegateExecution.setVariable("name", "new delhi");

        boolean weatherInfo = random.nextBoolean();



        LOGGER.info("weatherInfo = " + weatherInfo);

//        List<AccountDTO> accountDTOList = accountDataService.getAllAccount();

        List<AccountDTO> accountDTOList = new ArrayList<>();

        delegateExecution.setVariable("weatherOk", weatherInfo);

        delegateExecution.setVariable("accountList", accountDTOList);
    }
}
