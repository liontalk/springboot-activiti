package cn.liontalk.springbootactiviti6jpa;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RepositoryTest extends SpringbootActiviti6JpaApplicationTests {

    private static Logger logger = LoggerFactory.getLogger(RepositoryTest.class);


    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("processes/activiti.cfg.xml");


    /**
     * 部署流程文件资源库会增加文件  act_ge_bytearray
     */
    @Test
    public void testByteArray() {
        Deployment deployment = activitiRule.getRepositoryService().createDeployment()
                .name("二级审批流程")
                .addClasspathResource("processes/zhouzhe.bpmn")
                .deploy();
        logger.info("部署之后的id={}", deployment.getId());
    }


    /**
     * 手动往数据库中插入数据
     */
    @Test
    public void testSuspend() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        repositoryService.suspendProcessDefinitionById("myProcess_1:2:37504");
        boolean result = repositoryService.isProcessDefinitionSuspended("myProcess_1:2:37504");
        logger.info("流程是否被挂起：{}", result);
    }

}
