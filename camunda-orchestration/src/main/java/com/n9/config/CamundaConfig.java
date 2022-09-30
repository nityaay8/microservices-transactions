package com.n9.config;

import com.n9.service.camunda.tx.*;
import com.n9.service.camunda.CheckWeatherDelegate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaConfig {

    @Bean
    public CheckWeatherDelegate checkWeatherDelegate() {
        return new CheckWeatherDelegate();
    }

    @Bean
    public AccountsService accountsService() {
        return new AccountsService();
    }

    @Bean
    public DebitService debitService() {
        return new DebitService();
    }

    @Bean
    public ReverseDebitService reverseDebitService() {
        return new ReverseDebitService();
    }

    @Bean
    public CreditService creditService() {
        return new CreditService();
    }

    @Bean
    public LogTx logTx() {
        return new LogTx();
    }
}
