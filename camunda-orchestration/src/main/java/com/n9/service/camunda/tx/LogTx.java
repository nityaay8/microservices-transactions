package com.n9.service.camunda.tx;

import com.n9.service.camunda.PrintWeatherDetails;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.logging.Logger;

public class LogTx implements JavaDelegate {

    private final static Logger LOGGER = Logger.getLogger(LogTx.class.getName());

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        LOGGER.info(" \t ");

        LOGGER.info("logger delegate invoked by --start ");

        LOGGER.info("process definition id = " + delegateExecution.getProcessDefinitionId());
        LOGGER.info("active id = " + delegateExecution.getCurrentActivityId());
        LOGGER.info("active name = " + delegateExecution.getCurrentActivityName());
        LOGGER.info("process instance id = " + delegateExecution.getProcessInstanceId());
        LOGGER.info("business key = " + delegateExecution.getBusinessKey());
        LOGGER.info("execution id = " + delegateExecution.getId());

        LOGGER.info("all variables = " + delegateExecution.getVariables());

        LOGGER.info("logger delegate invoked by -- end");

        LOGGER.info(" \t ");

    }
}
