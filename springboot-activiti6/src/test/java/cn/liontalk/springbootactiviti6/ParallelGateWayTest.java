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

import javax.swing.*;
import java.util.List;

/**
 * 并行网关：
 * 流程任务首先通过一个节点，进行决策后，分别同时执行两个流程线，然后再归到另一个节点上统一走向结束节点，
 * 其中扮演分支和聚合工作的那两个节点，都属于并行网关。
 * 并行网关的特点就是在一个决策点将任务分支，然后同时执行多条任务线，最后再将多条任务线聚合在一起。
 * <p>
 * <p>
 * (1)一个流程示例只有一个，执行对象有多个
 * <p>
 * (2)并行网关的功能是基于进入和外出的顺序流的：
 *     分支(fork)：并行后的所有外出顺序流。为每个顺序流都创建一个并发分支。
 *     聚合(join)：所有到达并行网关，在此等待的进入分支，直到所有进入顺序流的分支都到达之后，流程就会通过汇聚网关。
 * <p>
 * (3)并行网关的进入和外出都是使用相同节点的标识。
 * <p>
 * (4)如果同一个并行网关有多个进入和多个外出顺序流，它就同时具有分支和汇聚功能。这时网关会先汇聚所有进入的顺序流，然后再切分成多个并行分支。
 * <p>
 * (5)并行网关不会解析条件，即使顺序流中定义了条件，也会被忽略。
 */
public class ParallelGateWayTest extends SpringbootActiviti6ApplicationTests {


    private static final String BPMN_RESOURCES_PATH = "processes/ParallelGateWay.bpmn";
    private static final String IMAGE_RESOURCES_PATH = "processes/ParallelGateWay.png";

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;


    /**
     * 运行结果：
     * 部署流程id：37501
     * 部署流程名称：发货收款---并行网关
     */
    @Test
    public void deployment() {

        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.name("发货收款---并行网关");
        deploymentBuilder.addClasspathResource(BPMN_RESOURCES_PATH);
        deploymentBuilder.addClasspathResource(IMAGE_RESOURCES_PATH);

        Deployment deployment = deploymentBuilder.deploy();
        System.out.println("部署流程id：" + deployment.getId());
        System.out.println("部署流程名称：" + deployment.getName());
    }


    /**
     * 开始流程
     */
    @Test
    public void startDefinitionProcess() {
        String processKey = "ParallelGateWay";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey);
        //流程实例ID
        System.out.println("流程实例ID :" + processInstance.getId());
        //流程定义ID
        System.out.println("流程定义ID :" + processInstance.getProcessDefinitionId());
    }

    /**
     * 运行结果：
     * 流程实例ID :40001
     * 流程定义ID :ParallelGateWay:1:37504
     */


    @Test
    public void findPersonalTask() {
        String buyer = "buyer";
        String seller = "seller";
        findMyTask(buyer);
        findMyTask(seller);
    }

    public void findMyTask(String assign) {
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
     * #############################################
     * 任务ID：40007
     * 任务名称：付款
     * 任务的创建时间：Thu Aug 22 13:36:12 CST 2019
     * 任务办理人：buyer
     * 流程实例ID：40001
     * 执行对象ID：40002
     * 流程定义ID：ParallelGateWay:1:37504
     * #############################################
     * #############################################
     * 任务ID：40010
     * 任务名称：发货
     * 任务的创建时间：Thu Aug 22 13:36:12 CST 2019
     * 任务办理人：seller
     * 流程实例ID：40001
     * 执行对象ID：40005
     * 流程定义ID：ParallelGateWay:1:37504
     * #############################################
     */


    @Test
    public void completeMyPersonalTask() {
        String buyerTaskId = "42504";
        String sellerTaskId = "42502";
        completeTask(buyerTaskId, sellerTaskId);
    }

    public void completeTask(String taskId, String sellerTaskId) {
        taskService.complete(taskId);
        taskService.complete(sellerTaskId);
        System.out.println("完了任务 ： " + taskId + " ,和任务 " + sellerTaskId);
    }

}
