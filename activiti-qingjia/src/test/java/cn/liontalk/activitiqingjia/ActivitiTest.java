package cn.liontalk.activitiqingjia;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiTest {


    /**
     * 从数据源和流程图中，生成一个数据库表（这个就是Activiti流程控制的关键的数据表）
     * 创建Activiti流的相关的数据库表
     */
    @Test
    public void createTable() {
        ProcessEngine processEngine = ProcessEngineConfiguration.
                createProcessEngineConfigurationFromResource("/processes/activiti.cfg.xml").buildProcessEngine();

    }

    /**
     * 1、部署流程
     * 2、启动流程实例
     * 3、请假人发出请假申请
     * 4、班主任查看任务
     * 5、班主任审批
     * 6、最终的教务处Boss审批
     */


    /**
     * 1：部署一个Activiti流程
     * 运行成功后，查看之前的数据库表，就会发现多了很多内容
     */
    @Test
    public void creatActivitiTask() {
        //加载的那两个内容就是我们之前已经弄好的基础内容哦。
        //得到了流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Deployment deployment = processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("processes/shengqing.bpmn")
                .name("请假申请")
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
        System.out.println(deployment.getKey());
    }


    /**
     * 2：启动流程实例
     */
    @Test
    public void testStartProcessInstance() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceById("shengqing:1:4");  //这个是查看数据库中act_re_procdef表
        System.out.println("流程实例ID：" + processInstance.getId());//流程实例ID
        System.out.println("流程定义ID：" + processInstance.getProcessDefinitionId());//流程定义ID
        System.out.println("流程激活ID: " + processInstance.getActivityId());
    }


    /**
     * 完成请假申请
     */
    @Test
    public void testQingjia() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService()
                .complete("5005"); //查看act_ru_task表
    }


    /**
     * 小明学习的班主任小毛查询当前正在执行任务
     */
    @Test
    public void testQueryTask() {
        //下面代码中的小毛，就是我们之前设计那个流程图中添加的班主任内容
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee("小毛")
                .list();
        for (Task task : tasks) {
            System.out.println(task.getName());
            System.out.println(task.getId());
        }
    }


    /**
     * 班主任小毛完成任务
     */
    @Test
    public void testFinishTask_manager() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        engine.getTaskService()
                .complete("7502"); //查看act_ru_task数据表
    }


    /**
     * 教务处的大毛完成的任务
     */
    @Test
    public void testFinishTask_Boss() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService()
                .complete("10002");  //查看act_ru_task数据表
    }


    /**
     *      涉及到的表
     * 	 *      act_ge_bytearray:
     * 	 *        1、英文解释
     * 	 *           act:activiti
     * 	 *           ge:general
     * 	 *           bytearray:二进制
     * 	 *        2、字段
     * 	 *           name_:文件的路径加上名称
     * 	 *           bytes_:存放内容
     * 	 *           deployment_id_:部署ID
     * 	 *        3、说明：
     * 	 *             如果要查询文件(bpmn和png)，需要知道deploymentId
     * 	 *      act_re_deployment
     * 	 *        1、解析
     * 	 *           re:repository
     * 	 *           deployment:部署  用户描述一次部署
     * 	 *        2、字段
     * 	 *            ID_：部署ID  主键
     * 	 *      act_re_procdef
     * 	 *        1、解释
     * 	 *            procdef: process definition  流程定义
     * 	 *        2、字段
     * 	 *            id_:pdid:pdkey:pdversion:随机数
     * 	 *            name:名称
     * 	 *            key:名称
     * 	 *            version:版本号
     * 	 *                如果名称不变，每次部署，版本号加1
     * 	 *                如果名称改变，则版本号从1开始计算
     * 	 *            deployment_id_:部署ID
     *
     */

    /**
     * 根据名称查询流程部署
     */
    @Test
    public void testQueryDeploymentByName(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Deployment> deployments = processEngine.getRepositoryService()
                .createDeploymentQuery()
                .orderByDeploymenTime()//按照部署时间排序
                .desc()//按照降序排序
                .deploymentName("请假流程")
                .list();
        for (Deployment deployment : deployments) {
            System.out.println(deployment.getId());
        }
    }
}
