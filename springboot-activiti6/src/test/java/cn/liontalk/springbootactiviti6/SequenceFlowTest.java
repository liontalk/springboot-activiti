package cn.liontalk.springbootactiviti6;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SequenceFlowTest extends SpringbootActiviti6ApplicationTests {


    private static final Logger log = LoggerFactory.getLogger(SequenceFlowTest.class);

    public static final String SEQUENCEFLOW_BPMN_PATH = "processes/SequenceFlow.bpmn";

    public static final String SEQUENCEFLOW_PNG_PATH = "processes/SequenceFlow.png";

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;


    @Autowired
    private TaskService taskService;


    /**
     * 具有分支的连线部署
     * <p>
     * 部署ID: 17501
     * 部署名称: 连线测试部署
     */
    @Test
    public void deploymentProcessDefinitionInputStream() {
        //  InputStream bpmnInputStream = this.getClass().getClassLoader().getResourceAsStream(SEQUENCEFLOW_BPMN_PATH);
        //  InputStream pngInputStream = this.getClass().getClassLoader().getResourceAsStream(SEQUENCEFLOW_PNG_PATH);
        //创建一个部署对象
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.name("连线测试部署");
        deploymentBuilder.addClasspathResource(SEQUENCEFLOW_BPMN_PATH);
        deploymentBuilder.addClasspathResource(SEQUENCEFLOW_PNG_PATH);
        Deployment deployment = deploymentBuilder.deploy();
        //打印流程信息
        System.out.println("部署ID: " + deployment.getId());
        System.out.println("部署名称: " + deployment.getName());

        /**
         * 部署ID: 2501
         * 部署名称: 连线测试部署
         *
         */
    }


    @Test
    public void startProcessInstance() {
        //获取流程启动Service
        // RuntimeService runtimeService=processEngine.getRuntimeService();
        //使用流程定义的key，key对应bpmn文件对应的id，
        //(也是act_re_procdef表中对应的KEY_字段),默认是按照最新版本启动
        String processDefinitionKey = "SequenceFlow";//流程定义的key就是sequenceFlow
        //获取流程实例对象
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程实例ID：" + processInstance.getId());//流程实例ID
        System.out.println("流程定义ID：" + processInstance.getProcessDefinitionId());//流程定义ID
        /**
         * 流程实例ID：5001
         * 流程定义ID：SequenceFlow:1:2504
         */
    }


    /**
     * #############################################
     * 任务ID：5005
     * 任务名称：审批【部门经理】
     * 任务的创建时间：Wed Aug 21 16:14:37 CST 2019
     * 任务办理人：zhaoliu
     * 流程实例ID：5001
     * 执行对象ID：5002
     * 流程定义ID：SequenceFlow:1:2504
     * #############################################
     */
    @Test
    public void findMyPersonTask() {
        String assign = "tianqi";
        List<Task> list = taskService.createTaskQuery() //创建任务查询对象
                .taskAssignee(assign) //指定个人任务查询，指定办理人
                .list();//获取该办理人下面的所有的任务的列表
        if (!CollectionUtil.isEmpty(list)) {
            for (Task task : list) {
                System.out.println("任务ID：" + task.getId());
                System.out.println("任务名称：" + task.getName());
                System.out.println("任务的创建时间：" + task.getCreateTime());
                System.out.println("任务办理人：" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID：" + task.getExecutionId());
                System.out.println("流程定义ID：" + task.getProcessDefinitionId());
                System.out.println("#############################################");
            }
        }
    }


    /**
     * 完成任务
     */
    @Test
    public void testCompleteTask(){
        String taskId = "5005";
        Map<String,Object> variables=new HashMap<String,Object>();
        variables.put("message","重要");
        taskService.complete(taskId,variables);
        System.out.println("任务完成了===》 " + taskId);
    }
}
