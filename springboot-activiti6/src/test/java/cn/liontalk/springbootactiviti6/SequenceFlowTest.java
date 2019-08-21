package cn.liontalk.springbootactiviti6;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.omg.SendingContext.RunTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.annotation.XmlType;
import java.io.InputStream;

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
     *
     * 部署ID: 17501
     * 部署名称: 连线测试部署
     *
     */
    @Test
    public void deploymentProcessDefinitionInputStream() {
        InputStream bpmnInputStream = this.getClass().getClassLoader().getResourceAsStream(SEQUENCEFLOW_BPMN_PATH);
        InputStream pngInputStream = this.getClass().getClassLoader().getResourceAsStream(SEQUENCEFLOW_PNG_PATH);
        //创建一个部署对象
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.name("连线测试部署");
        deploymentBuilder.addInputStream("bpmnInputStream", bpmnInputStream);
        deploymentBuilder.addInputStream("pngInputStream", pngInputStream);

        Deployment deployment = deploymentBuilder.deploy();

        //打印流程信息
        System.out.println("部署ID: " + deployment.getId());
        System.out.println("部署名称: " + deployment.getName());
    }



    @Test
    public void startProcessInstance(){
        //获取流程启动Service
       // RuntimeService runtimeService=processEngine.getRuntimeService();
        //使用流程定义的key，key对应bpmn文件对应的id，
        //(也是act_re_procdef表中对应的KEY_字段),默认是按照最新版本启动
        String processDefinitionKey="SequenceFlow";//流程定义的key就是sequenceFlow
        //获取流程实例对象
        ProcessInstance processInstance=runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程实例ID："+processInstance.getId());//流程实例ID
        System.out.println("流程定义ID："+processInstance.getProcessDefinitionId());//流程定义ID
    }
}
