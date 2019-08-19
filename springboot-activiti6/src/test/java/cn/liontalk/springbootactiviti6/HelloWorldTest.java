package cn.liontalk.springbootactiviti6;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HelloWorldTest extends SpringbootActiviti6ApplicationTests {


    private static Logger log = LoggerFactory.getLogger(HelloWorldTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("processes/activiti.cfg.xml");

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinition() {
        //与流程定义和部署对象相关的Service
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();//创建一个部署对象
        deploymentBuilder.name("helloWorld入门程序");//添加部署的名称
        deploymentBuilder.addClasspathResource("processes/HelloWorld.bpmn");//从classpath的资源加载，一次只能加载一个文件
        deploymentBuilder.addClasspathResource("processes/HelloWorld.png");//从classpath的资源加载，一次只能加载一个文件
        Deployment deployment = deploymentBuilder.deploy();//完成部署
        //打印我们的流程信息
        System.out.println("流程Id:" + deployment.getId());
        System.out.println("流程Name:" + deployment.getName());

        //参看resources 目录下面的 image -- 部署任务之后影响数据库.png
    }


    /**
     * 部署的流程实例进行启动
     */
    @Test
    public void startProcessInstance() {
        //获取流程启动Service
        RuntimeService runtimeService = activitiRule.getRuntimeService();

        //使用流程定义的key，key对应bpmn文件对应的id，
        //(也是act_re_procdef表中对应的KEY_字段),默认是按照最新版本启动
        String processDefinitionkey = "HelloWorld";//流程定义的key就是HelloWorld
        //获取流程实例对象
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionkey);
        System.out.println("流程实例ID：" + processInstance.getId());//流程实例ID
        System.out.println("流程定义ID：" + processInstance.getProcessDefinitionId());//流程定义ID

        //参看resources 目录下面的 image -- 流程开始01.png 和 流程开始02.png
    }


    /**
     * 查询当前的个人任务(实际就是查询act_ru_task表)
     */
    @Test
    public void findMyPersonalTask() {
        String assignee = "zhouzhe";
        //获取事务Service
        TaskService taskService = activitiRule.getTaskService();
        List<Task> taskList = taskService.createTaskQuery()//创建任务查询对象
                .taskAssignee(assignee)//指定个人任务查询，指定办理人
                .list();//获取该办理人下的事务列表
        if (taskList != null && taskList.size() > 0) {
            for (Task task : taskList) {
                log.info("##########################################################################################");
                log.info("任务ID：" + task.getId());
                log.info("任务名称：" + task.getName());
                log.info("任务的创建时间：" + task.getCreateTime());
                log.info("任务办理人：" + task.getAssignee());
                log.info("流程实例ID：" + task.getProcessInstanceId());
                log.info("执行对象ID：" + task.getExecutionId());
                log.info("流程定义ID：" + task.getProcessDefinitionId());
                log.info("##########################################################################################");
            }
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void completeMyPersonalTask() {
        TaskService taskService = activitiRule.getTaskService();
        String taskId = "2505";//上一次我们查询的任务ID就是304
        taskService.complete(taskId);//完成taskId对应的任务
        System.out.println("完成ID为" + taskId + "的任务");

        //[结果] 完成ID为2505的任务
    }


    /**
     * 查询当前的个人任务(实际就是查询act_ru_task表)
     */
    @Test
    public void findMyPersonalTaskByLiSi() {
        String assignee = "lisi";
        //获取事务Service
        TaskService taskService = activitiRule.getTaskService();
        List<Task> taskList = taskService.createTaskQuery()//创建任务查询对象
                .taskAssignee(assignee)//指定个人任务查询，指定办理人
                .list();//获取该办理人下的事务列表
        if (taskList != null && taskList.size() > 0) {
            for (Task task : taskList) {
                log.info("##########################################################################################");
                System.out.println(("任务ID：" + task.getId()));
                System.out.println(("任务名称：" + task.getName()));
                System.out.println(("任务的创建时间：" + task.getCreateTime()));
                System.out.println(("任务办理人：" + task.getAssignee()));
                System.out.println(("流程实例ID：" + task.getProcessInstanceId()));
                System.out.println(("执行对象ID：" + task.getExecutionId()));
                System.out.println(("流程定义ID：" + task.getProcessDefinitionId()));
                log.info("##########################################################################################");
            }
        }
        /**
         * 结果：
         *
         * 任务ID：5002
         * 任务名称：审批【部门经理】
         * 任务的创建时间：Mon Aug 19 15:38:59 CST 2019
         * 任务办理人：lisi
         * 流程实例ID：2501
         * 执行对象ID：2502
         * 流程定义ID：HelloWorld:1:4
         *
         */
    }



    /**
     * 完成任务
     */
    @Test
    public void completeMyPersonalTaskByLiSi() {
        TaskService taskService = activitiRule.getTaskService();
        String taskId = "5002";//上一次我们查询的任务ID就是304
        taskService.complete(taskId);//完成taskId对应的任务
        System.out.println("完成ID为" + taskId + "的任务");
        //[结果] 完成ID为5002的任务
    }

    /**
     * 查询当前的个人任务(实际就是查询act_ru_task表)
     */
    @Test
    public void findMyPersonalTaskWangWu() {
        String assignee = "wangwu";
        //获取事务Service
        TaskService taskService = activitiRule.getTaskService();
        List<Task> taskList = taskService.createTaskQuery()//创建任务查询对象
                .taskAssignee(assignee)//指定个人任务查询，指定办理人
                .list();//获取该办理人下的事务列表
        if (taskList != null && taskList.size() > 0) {
            for (Task task : taskList) {
                log.info("##########################################################################################");
                log.info("任务ID：" + task.getId());
                log.info("任务名称：" + task.getName());
                log.info("任务的创建时间：" + task.getCreateTime());
                log.info("任务办理人：" + task.getAssignee());
                log.info("流程实例ID：" + task.getProcessInstanceId());
                log.info("执行对象ID：" + task.getExecutionId());
                log.info("流程定义ID：" + task.getProcessDefinitionId());
                log.info("##########################################################################################");
            }
        }

        /**
         * 
         *  任务ID：7502
         *  任务名称：审批【总经理】
         *  任务的创建时间：Mon Aug 19 15:44:07 CST 2019
         *  任务办理人：wangwu
         *  流程实例ID：2501
         *  执行对象ID：2502
         *  流程定义ID：HelloWorld:1:4
         * 
         */
    }



    /**
     * 完成任务
     */
    @Test
    public void completeMyPersonalTaskByWangWu() {
        TaskService taskService = activitiRule.getTaskService();
        String taskId = "7502";//上一次我们查询的任务ID就是304
        taskService.complete(taskId);//完成taskId对应的任务
        System.out.println("完成ID为" + taskId + "的任务");
        //[结果] 完成ID为 [7502] 的任务
    }
}
