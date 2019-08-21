package cn.liontalk.springbootactiviti6;

import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ProcessEngineCreateTableAndDropTableTest extends SpringbootActiviti6ApplicationTests {


    public static final Logger log = LoggerFactory.getLogger(ProcessEngineCreateTableAndDropTableTest.class);

    public static final String CONFIG_PATH = "processes/activiti.cfg.xml";

    @Test
    public void createTable_2() {
        //流程引擎ProcessEngine对象，所有操作都离不开引擎对象
        ProcessEngineConfiguration processEngineConfiguration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(CONFIG_PATH);
        //获取工作流的核心对象，ProcessEngine对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        System.out.println("processEngine:" + processEngine + "Create Success!!");
    }


    @Test
    public void createTable() {
        try {
            ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(CONFIG_PATH).buildProcessEngine();
            log.info("创建表格成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除表格
     */
    @Test
    public void deleteTable() {
        ProcessEngine processEngine = null;
        try {
            processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(CONFIG_PATH).buildProcessEngine();
            ManagementService managementService = processEngine.getManagementService();
            managementService.executeCommand(commandContext -> {
                commandContext.getDbSqlSession().dbSchemaDrop();
                return null;
            });
            log.info("删除表格成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
