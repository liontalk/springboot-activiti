package cn.liontalk.springbootactiviti6;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

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


    /**
     * 查看流程定义
     * id:(key):(version):(随机值)
     * name:对应流程文件process节点的name属性
     * key:对应流程文件process节点的id属性
     * version:发布时自动生成的。如果是第一次发布的流程，version默认从1开始；
     * 如果当前流程引擎中已存在相同的流程，则找到当前key对应的最高版本号，在最高版本号上加1
     * <p>
     * <p>
     * <p>
     * 说明：
     * 1)流程定义和部署对象相关的Service都是RepositoryService。
     * <p>
     * 2)创建流程定义查询对象，可以在ProcessDefinitionQuery上设置查询的相关参数
     * <p>
     * 3)调用ProcessDefinitionQuery对象的list方法，执行查询，获得符合条件的流程定义列表
     * <p>
     * 4)由运行结果可以看出：
     * Key和Name的值为：bpmn文件process节点的id和name的属性值
     * <p>
     * <p>
     * 5)key属性被用来区别不同的流程定义。
     * <p>
     * 6)带有特定key的流程定义第一次部署时，version为1。之后每次部署都会在当前最高版本号上加1
     * <p>
     * 7)Id的值的生成规则为:{processDefinitionKey}:{processDefinitionVersion}:{generated-id},
     * 这里的generated-id是一个自动生成的唯一的数字
     * <p>
     * 8)重复部署一次，deploymentId的值以一定的形式变化
     *    规则act_ge_property表生成
     * <p>
     * ————————————————
     * 版权声明：本文为CSDN博主「光仔December」的原创文章，遵循CC 4.0 by-sa版权协议，转载请附上原文出处链接及本声明。
     * 原文链接：https://blog.csdn.net/acmman/article/details/60981267
     */
    @Test
    public void queryProcessDefinition() throws Exception {

        RepositoryService repositoryService = activitiRule.getRepositoryService();

        //获取仓库服务对象，使用版本的升级排列，查询列表
        List<ProcessDefinition> pdList = repositoryService
                .createProcessDefinitionQuery()
                //添加查询条件
                //.processDefinitionId(processDefinitionId)
                //.processDefinitionKey(processDefinitionKey)
                //.processDefinitionName(processDefinitionName)
                //排序(可以按照id/key/name/version/Cagetory排序)
                .orderByProcessDefinitionVersion().asc()
                //.count()
                //.listPage(firstResult, maxResults)
                //.singleResult()
                .list();//总的结果集数量
        //便利集合，查看内容
        for (ProcessDefinition pd : pdList) {
            System.out.println("###########################################");
            System.out.println("id:" + pd.getId());
            System.out.println("name:" + pd.getName());
            System.out.println("key:" + pd.getKey());
            System.out.println("version:" + pd.getVersion());
            System.out.println("resourceName:" + pd.getDiagramResourceName());
            System.out.println("###########################################");
        }
    }


    /**
     * 通过zip 部署流程定义
     */
    @Test
    public void deploymentProcessDefinitionByZip() {
        //获得上传文件的输入流程
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("processes/HelloWorld.zip");
        if (null != inputStream) {
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            //获取仓库服务
            RepositoryService repositoryService = activitiRule.getRepositoryService();
            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
            deploymentBuilder.name("HelloWorld部署zip文件");
            deploymentBuilder.addZipInputStream(zipInputStream);
            Deployment deployment = deploymentBuilder.deploy();
            //打印部署信息
            System.out.println("部署 ： " + deployment.getId());
            System.out.println("部署名称 ；" + deployment.getName());
        } else {
            System.out.println("获得数据的信息为空。。。。。");
        }
    }


    /**
     * 删除正在部署的流程信息数据
     * <p>
     * 说明：
     * 1)因为删除的是流程定义，而流程定义的部署是属于仓库服务的，所以应该先得到RepositoryService
     * 2)如果该流程定义下没有正在运行的流程，则可以用普通删除。如果是有关联的信息，用级联删除。
     * 项目开发中使用级联删除的情况比较多，删除操作一般只开放给超级管理员使用。
     */
    @Test
    public void testDeleteDeploymentData() {
        String deploymentId = "22501";
        //获取仓库服务对象
        RepositoryService repositoryService = activitiRule.getRepositoryService();

        //普通删除，如果有正在执行的流程，则抛出异常
        //repositoryService.deleteDeployment(deploymentId);

        //级联删除，会删除和当前规则先关的所有的信息。正在执行的信息也包括历史信息
        repositoryService.deleteDeployment(deploymentId, true);

        System.out.println("删除成功：" + deploymentId);

    }


    /**
     * 查看附件流程图片
     */
    @Test
    public void viewImage() {
        String deploymentId = "25001";
        List<String> names = activitiRule.getRepositoryService().getDeploymentResourceNames(deploymentId);
        String imageName = null;
        for (String name : names) {
            System.out.println("获得数据的名称是：" + name);
            if (name.lastIndexOf(".png") > 0) {
                imageName = name;
                break;
            }
        }
        System.out.println("imageName ==  >" + imageName);
    }
}
