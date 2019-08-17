package cn.liontalk.springbootactiviti6jpa;

import org.activiti.engine.ManagementService;
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


public class DbConfigTest extends SpringbootActiviti6JpaApplicationTests {

    private static Logger logger = LoggerFactory.getLogger(DbConfigTest.class);


    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("processes/activiti.cfg.xml");


    /**
     * 部署流程文件资源库会增加文件  act_ge_bytearray
     */
    @Test
    public void testByteArray() {
        Deployment deployment = activitiRule.getRepositoryService().createDeployment()
                .name("测试部署")
                .addClasspathResource("processes/qingjia.bpmn20.xml")
                .deploy();
        logger.info("部署之后的id={}", deployment.getId());
    }


    /**
     * 手动往数据库中插入数据
     */
    @Test
    public void testByteArrayInsert() {
        ManagementService managementService = activitiRule.getManagementService();
        Object object = managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                ByteArrayEntity byteArrayEntity = new ByteArrayEntityImpl();
                byteArrayEntity.setName("zhouzhe");
                byteArrayEntity.setBytes("TEST".getBytes());
                commandContext.getByteArrayEntityManager().insert(byteArrayEntity);
                logger.info("手动部署的...........");
                return null;
            }
        });
    }

}
