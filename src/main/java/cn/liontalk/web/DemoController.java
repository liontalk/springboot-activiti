package cn.liontalk.web;

import org.activiti.bpmn.model.Task;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhouZhe
 * @version 1.0
 * @description DemoController
 * @date 2019-05-20 21:28
 **/
@RequestMapping(value = "/demo")
@RestController
public class DemoController {




    @RequestMapping(value = "/first")
    public void firstDemo(){

        //根据bpmn文件部署流程
//        Deployment deployment = repositoryService.createDeployment().addClasspathResource("demo2.bpmn").deploy();
//        //获取流程定义
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
//        //启动流程定义，返回流程实例
//        ProcessInstance pi = runtimeService.startProcessInstanceById(processDefinition.getId());
//        String processId = pi.getId();
//        System.out.println("流程创建成功，当前流程实例ID："+processId);
//
//        Task task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
//        System.out.println("第一次执行前，任务名称："+task.getName());
//        taskService.complete(task.getId());
//
//        task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
//        System.out.println("第二次执行前，任务名称："+task.getName());
//        taskService.complete(task.getId());
//
//        task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
//        System.out.println("task为null，任务执行完毕："+task);


    }


}
