package com.n9.service.camunda.tx;

import com.n9.service.camunda.PrintWeatherDetails;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


@Slf4j
public class LogTx implements JavaDelegate {


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        log.info(" \t ");

        log.info("log delegate invoked by --start ");

        log.info("process definition id = " + delegateExecution.getProcessDefinitionId());
        log.info("active id = " + delegateExecution.getCurrentActivityId());
        log.info("active name = " + delegateExecution.getCurrentActivityName());
        log.info("process instance id = " + delegateExecution.getProcessInstanceId());
        log.info("business key = " + delegateExecution.getBusinessKey());
        log.info("execution id = " + delegateExecution.getId());

        log.info("all variables = " + delegateExecution.getVariables());

        log.info("log delegate invoked by -- end");

        log.info(" \t ");

    }
}
