package cn.liontalk.springbootactiviti6;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;


public class RuntimeServiceTest extends SpringbootActiviti6ApplicationTests {


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

        /**
         * 结果:
         * 流程Id:1
         * 流程Name:helloWorld入门程序
         *
         *
         * 操作表： act_re_procdef
         *         act_ge_bytearray
         *         act_re_deployment
         */
    }


    /**
     * 启动流程
     * 当流程到达一个节点时，会在act_ru_execution表中产生1条数据
     * 如果当节点是用户任务节点，这时会在act_ru_task表中产生1条数据
     */
    @Test
    public void startProcess() throws Exception {
        //按照流程定义的id启动：Helloworld:1:4 (只启动此版本流程，最高版本是HelloWorld:2:704)
        //runtimeService.startProcessInstanceById(processDefinitionId);
        //使用流程定义的key启动流程，默认会启动最高版本的流程
        String processDefinitionkey = "HelloWorld";//流程定义的key就是HelloWorld
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        //获取流程实例对象
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionkey);
        System.out.println("processInstance========>" + processInstance);
        System.out.println("pid========>" + processInstance.getId());
        System.out.println("activitiId::" + processInstance.getActivityId());
        System.out.println("pdid:======>" + processInstance.getProcessDefinitionId());

        /***
         *
         * 结果：
         * pid========>2501
         * activitiId::null
         * pdid:======>HelloWorld:1:4
         *
         * act_ru_execution表是正在执行的执行对象表。
         * 其中ID_是执行对象ID，PROC_INST_ID是流程实例ID。
         *
         * 看图 resources/image  中的 RunTimeService开始流程01.png
         */
    }


    /**
     * 查询当前的个人任务(实际就是查询act_ru_task表)
     */
    @Test
    public void findMyPersonalTask() {
        String assignee = "wangwu";
        //获取事务Service
        TaskService taskService = activitiRule.getTaskService();
        List<Task> taskList = taskService.createTaskQuery()//创建任务查询对象
                .taskAssignee(assignee)//指定个人任务查询，指定办理人
                .list();//获取该办理人下的事务列表
        if (taskList != null && taskList.size() > 0) {
            for (Task task : taskList) {
                System.out.println("#############################################");
                System.out.println("任务ID：" + task.getId());
                System.out.println("任务名称：" + task.getName());
                System.out.println("任务的创建时间：" + task.getCreateTime());
                System.out.println("任务办理人：" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID：" + task.getExecutionId());
                System.out.println("流程定义ID：" + task.getProcessDefinitionId());
                System.out.println("#############################################");
            }
        }else{
            System.out.println("################## 没有需要完成的任务 #################");
            System.out.println("################## 没有需要完成的任务 #################");
        }

        /**
         *
         *#############################################
         * 任务ID：2505
         * 任务名称：提交申请
         * 任务的创建时间：Tue Aug 20 15:34:16 CST 2019
         * 任务办理人：zhouzhe
         * 流程实例ID：2501
         * 执行对象ID：2502
         * 流程定义ID：HelloWorld:1:4
         * #############################################
         * #############################################
         * 任务ID：5005
         * 任务名称：提交申请
         * 任务的创建时间：Tue Aug 20 15:47:40 CST 2019
         * 任务办理人：zhouzhe
         * 流程实例ID：5001
         * 执行对象ID：5002
         * 流程定义ID：HelloWorld:1:4
         * #############################################
         */
    }


    /**
     * 完成我的任务
     */
    @Test
    public void completeMyPersonalTask() {
        String taskId = "10002";//上一次我们查询的任务ID就是804
        TaskService taskService = activitiRule.getTaskService();
        taskService.complete(taskId);//完成taskId对应的任务
        System.out.println("完成ID为" + taskId + "的任务");
    }

}
