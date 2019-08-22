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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 什么是网关(GateWay)? 其实网关说白了就是事件流到某一个核心节点，该节点需要做一个判断，
 * 如果判断符合某一个逻辑，那么事件就流到合适的路径上去，进行了分支。而做判断的节点就是所谓的网关。
 */

/**
 * 1)一个排他网关对应一个以上的顺序流。
 * 2)由排他网关流出的顺序流都有一个conditionExpression元素，在内部维护返回boolean类型的决策结果。
 * 3)决策网关只会返回一条结果。当流程执行到排他网关时，流程引擎会自动检索网关出口，从上到下检索，
 * 如果发现第一条决策结果为true或者没有设置条件的(默认成立)，则流出。
 * 4)如果没有任何一个出口符合条件，则抛出异常。
 * 5)使用流程变量，设置连线的条件，并按照连线的条件执行工作流，如果没有符合的条件，则执行默认的连线。
 */

public class ExclusiveGateWayTest extends SpringbootActiviti6ApplicationTests {

    private static final String BPMN_RESOURCES_PATH = "processes/ExclusiveGateWay.bpmn";
    private static final String IMAGE_RESOURCES_PATH = "processes/ExclusiveGateWay.png";

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;


    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinition() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.name("费用报销申请--排他网关");
        deploymentBuilder.addClasspathResource(BPMN_RESOURCES_PATH);
        deploymentBuilder.addClasspathResource(IMAGE_RESOURCES_PATH);
        Deployment deployment = deploymentBuilder.deploy();
        System.out.println("部署id： " + deployment.getId());
        System.out.println("部署名称：" + deployment.getName());
        System.out.println("部署版本：" + deployment.getKey());
        System.out.println("部署目录：" + deployment.getCategory());
    }


    @Test
    public void startProcessDefinition() {
        //使用流程定义的key，key对应bpmn文件对应的id，
        //(也是act_re_procdef表中对应的KEY_字段),默认是按照最新版本启动
        String processDefinitionKey = "MoneyApply";//流程定义的key就是myProces
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        //流程实例ID
        System.out.println("流程实例ID :" + processInstance.getId());
        //流程定义ID
        System.out.println("流程定义ID :" + processInstance.getProcessDefinitionId());

        /**
         *
         * 结果
         * 流程实例ID :20001
         * 流程定义ID :MoneyApply:1:17504
         */
    }


    /**
     * 查询当前的个人任务(实际就是查询act_ru_task表)
     */
    @Test
    public void findMyPersonalTask() {
        String assign = "zhouzhe";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assign)
                .list();
        if (!CollectionUtil.isEmpty(list)) {
            for (Task task : list) {
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
        }
    }
    /**
     *
     * 任务ID：20005
     * 任务名称：费用申请报销
     * 任务的创建时间：Thu Aug 22 10:30:34 CST 2019
     * 任务办理人：zhouzhe
     * 流程实例ID：20001
     * 执行对象ID：20002
     * 流程定义ID：MoneyApply:1:17504
     *
     */


    /**
     * 完成我的任务 报销450元走财务审核
     */
    @Test
    public void completeMyPersonalTask() {
        //上一次完成工作我们查询的任务ID
        String taskId = "20005";
        //完成任务的同时，设置流程变量，使用流程变量用来制定完成任务后，下一个连线，
        //对应exclusiveGateWayFlow.bpmn文件中${message==450}
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", 450);
        taskService.complete(taskId, map);
        System.out.println("完成任务ID为：" + taskId + "的任务！");
    }


    /**
     * 根据财务去查找个人的人任务
     * <p>
     * 任务ID：22504
     * 任务名称：财务
     * 任务的创建时间：Thu Aug 22 10:40:41 CST 2019
     * 任务办理人：lichanghui
     * 流程实例ID：20001
     * 执行对象ID：20002
     * 流程定义ID：MoneyApply:1:17504
     */
    @Test
    public void findMyPersonalTaskByCaiWu() {
        String assign = "lichanghui";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assign)
                .list();
        if (!CollectionUtil.isEmpty(list)) {
            for (Task task : list) {
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
        }
    }


    @Test
    public void completeTaskByCaiWu() {
        String taskId = "22504";
        taskService.complete(taskId);
        System.out.println("完成了任务：" + taskId + "的任务");
    }


}
