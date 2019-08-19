package cn.liontalk.springbootactiviti6;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class ProcessEngineTest1 extends SpringbootActiviti6ApplicationTests {


    @Test
    public void createTable_2() {
        //流程引擎ProcessEngine对象，所有操作都离不开引擎对象
        ProcessEngineConfiguration processEngineConfiguration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("processes/activiti.cfg.xml");
        //获取工作流的核心对象，ProcessEngine对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        System.out.println("processEngine:" + processEngine + "Create Success!!");
    }

}
