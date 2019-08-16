import com.google.common.collect.Maps;
import org.activiti.bpmn.model.StringDataObject;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author ZhouZhe
 * @version 1.0
 * @description
 * @date 2019-08-15 21:37
 **/
public class DemoMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoMain.class);

    public static void main(String[] args) throws ParseException {


        LOGGER.info("启动程序");

        //创建流程引擎

        ProcessEngine processEngine = getProcessEngine();

        //部署流程定义文件
        ProcessDefinition processDefinition = getProcessDefinition(processEngine);


        //启动流程

        ProcessInstance processInstance = startProcess(processEngine, processDefinition);


        Scanner scanner = new Scanner(System.in);
        while (processInstance != null && !processInstance.isEnded()) {
            //处理流程任务
            TaskService taskService = processEngine.getTaskService();
            List<Task> list = taskService.createTaskQuery().list();

            Map<String, Object> variable = Maps.newHashMap();
            for (Task task : list) {
                LOGGER.info("待处理任务 {} ", task.getName());
                FormService formService = processEngine.getFormService();
                TaskFormData taskFormData = formService.getTaskFormData(task.getId());
                List<FormProperty> list1 = taskFormData.getFormProperties();
                for (FormProperty property : list1) {
                    String line = null;
                    if (StringFormType.class.isInstance(property.getType())) {
                        LOGGER.info("请输入{} ?", property.getName());
                        line = scanner.nextLine();
                        LOGGER.info("您输入的内容是{} ?", line);
                        variable.put(property.getId(), line);
                    } else if (DateFormType.class.isInstance(property.getType())) {
                        LOGGER.info("请输入{} ? 格式(yyyy-MM-dd HH:mm:ss)", property.getName());
                        line = scanner.nextLine();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = simpleDateFormat.parse(line);
                        variable.put(property.getId(), date);
                    } else {
                        LOGGER.info("您输入的类型暂不支持", property.getName());
                    }
                }
                //提交表单
                taskService.complete(task.getId(), variable);
                processInstance = processEngine.getRuntimeService().createProcessInstanceQuery()
                        .processInstanceId(processInstance.getId()).singleResult();
            }
            LOGGER.info("待处理任务数量 {} ", list.size());
        }
        LOGGER.info("结束程序");
    }

    private static ProcessInstance startProcess(ProcessEngine processEngine, ProcessDefinition processDefinition) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        LOGGER.info("启动流程{}", processInstance.getProcessDefinitionKey());
        return processInstance;
    }

    private static ProcessEngine getProcessEngine() {
        ProcessEngineConfiguration processEngineConfiguration =
                ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        String name = processEngine.getName();
        String version = ProcessEngine.VERSION;
        LOGGER.info("name=={}", name);
        LOGGER.info("version=={}", version);
        return processEngine;
    }

    private static ProcessDefinition getProcessDefinition(ProcessEngine processEngine) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource("second_approve.bpmn20.xml");
        Deployment deployment = deploymentBuilder.deploy();
        String deploymentId = deployment.getId();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();

        LOGGER.info("流程定义对象=={},id={}", processDefinition.getName(), processDefinition.getId());
        return processDefinition;
    }
}
