package com.n9.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("weather")
public class WeatherController {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity weatherCheker() {
        ResponseEntity responseEntity;
        String processId = "weather_checker:1:550dd733-4013-11ed-85ab-94e23c865281";
//        processId = "weather_checker";
//        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processId);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("weather_checker");
        responseEntity = ResponseEntity.ok("process started, id = " + processInstance.getProcessInstanceId());
        return responseEntity;
    }
}
