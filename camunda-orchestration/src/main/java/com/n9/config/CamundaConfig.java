package com.n9.config;

import com.n9.service.camunda.CheckWeatherDelegate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaConfig {

    @Bean
    public CheckWeatherDelegate checkWeatherDelegate() {
        return new CheckWeatherDelegate();
    }
}
