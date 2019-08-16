package cn.liontalk.springbootactiviti6jpa.controller;


import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/demo")
public class DemoController {

    private static Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;


    @Autowired
    TaskService taskService;


    @RequestMapping(value = "/first")
    public String firstDemo() {

        //部署bpmn文件流程
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/qingjia.bpmn20.xml")
                .deploy();

        //act_ge_bytearray 表格中插入数据
        //act_re_deployment 表格中插入数据
        logger.info("deployment的数据信息是={}", deployment);

        //获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();
        logger.info("processDefinition的数据信息是:{}", processDefinition);


        //启动流程定义
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        String processId = processInstance.getId();
        logger.info("流程信息数据是={}", processInstance);
        logger.info("流程创建成功，当前流程实例ID={}", processId);


        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        logger.info("第一次任务信息是={}", task);
        logger.info("第一次任务执行前,任务名称={},任务id={}", task.getName(), task.getId());
        taskService.complete(task.getId());


        task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        logger.info("第二次任务信息是={}", task);
        logger.info("第二次任务执行前,任务名称={},任务id={}", task.getName(), task.getId());
        taskService.complete(task.getId());

        task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        logger.info("第二次任务信息是={}", task);
        return "OK";
    }
}
