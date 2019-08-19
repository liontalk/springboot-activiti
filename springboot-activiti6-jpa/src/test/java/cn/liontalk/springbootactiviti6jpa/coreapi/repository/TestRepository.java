package cn.liontalk.springbootactiviti6jpa.coreapi.repository;

import cn.liontalk.springbootactiviti6jpa.SpringbootActiviti6JpaApplicationTests;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class TestRepository extends SpringbootActiviti6JpaApplicationTests {


    public static final Logger log = LoggerFactory.getLogger(TestRepository.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("processes/activiti.cfg.xml");


    @Test
    public void testRepository() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.name("测试部署资源")
                .addClasspathResource("processes/zhouzhe.bpmn")
                .addClasspathResource("processes/test.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        log.info("deploy=={}", deploy);


        DeploymentBuilder deploymentBuilder1 = repositoryService.createDeployment();
        deploymentBuilder1.name("测试部署资源2")
                .addClasspathResource("processes/zhouzhe.bpmn")
                    .addClasspathResource("processes/test.bpmn20.xml");
        Deployment deploy1 = deploymentBuilder1.deploy();
        log.info("deploy=={}", deploy1);


        //部署完成之后进行查询
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        List<Deployment> deploymentList = deploymentQuery
                //.deploymentId(deploy.getId())
                //.singleResult();
                .orderByDeploymenTime().asc()
                .listPage(0, 100);


        for (Deployment deployment : deploymentList) {
            log.info("deployment {} ", deployment);
        }
        log.info("deployment.size = {} ", deploymentList.size());
        //流程定义的对象
        List<ProcessDefinition> processDefiList = repositoryService
                .createProcessDefinitionQuery()
                //.deploymentId(deployment.getId())
                .orderByProcessDefinitionKey().asc()
                .listPage(0, 100);
        log.info("processDefiList的数量大小{} ", processDefiList.size());
        for (ProcessDefinition definition : processDefiList) {
            log.info("processDefinition = {} ,version = {} ,key = {}, id = {}",
                    definition,
                    definition.getVersion(),
                    definition.getKey(),
                    definition.getId());
        }
    }


    @Test
    @org.activiti.engine.test.Deployment(resources = {"processes/test.bpmn20.xml"})
    public void testSuspend() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        //获取唯一的一个结果
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery().listPage(0, 100);
        log.info("definitionList size={} ", definitionList.size());
        ProcessDefinition definition = definitionList.get(0);
        for (ProcessDefinition definitions : definitionList) {
            log.info("definitions == {}", definitions);
        }

        //挂起
        repositoryService.suspendProcessDefinitionById(definition.getId());
        try {
            log.info("启动");
            activitiRule.getRuntimeService().startProcessInstanceById(definition.getId());
            log.info("开始启动");
        } catch (Exception e) {
            log.error("启动失败！");
            e.printStackTrace();
        }


        //激活
        repositoryService.activateProcessDefinitionById(definition.getId());
        //激活之后再次去尝试启动的过程
        try {
            log.info("激活后启动");
            activitiRule.getRuntimeService().startProcessInstanceById(definition.getId());
            log.info("激活后开始启动");
        } catch (Exception e) {
            log.error("激活后启动失败！");
            e.printStackTrace();
        }
    }


    @Test
    @org.activiti.engine.test.Deployment(resources = {"processes/test.bpmn20.xml"})
    public void testCandidateStarter() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        //获取唯一的一个结果
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery().listPage(0, 100);
        log.info("definitionList size={} ", definitionList.size());
        ProcessDefinition definition = definitionList.get(0);
        for (ProcessDefinition definitions : definitionList) {
            log.info("definitions == {}", definitions);
        }
        //设置用户
        repositoryService.addCandidateStarterUser(definition.getId(), "user1");
        //设置用户组
        repositoryService.addCandidateStarterGroup(definition.getId(), "groupM");
        List<IdentityLink> list = repositoryService.getIdentityLinksForProcessDefinition(definition.getId());
        for (IdentityLink identity : list) {
            log.info("identity  = {}", identity);
        }
        //删除用户组
        repositoryService.deleteCandidateStarterGroup(definition.getId(), "groupM");

        //删除用户
        repositoryService.deleteCandidateStarterUser(definition.getId(), "user1");
    }
}
