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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

public class HelloWorldTest extends SpringbootActiviti6ApplicationTests {

    private static Logger log = LoggerFactory.getLogger(HelloWorldTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("processes/activiti.cfg.xml");

    /**
     * 部署流程定义
     *
     *
     *  a)act_re_deployment（部署对象表）
     *         存放流程定义的显示名和部署时间，每部署一次增加一条记录
     *     注：如果部署相同Key的流程，那么Version将会升级，也就是版本升级:
     *     启动该Key的流程时，默认启动最新版本(Version)的流程。
     *
     *
     *     b)act_re_procdef（流程定义表）
     *         存放流程定义的属性信息，部署每个新的流程定义都会在这张表中增加一条记录。
     *     注意：当流程定义的key相同的情况下，使用的是版本升级。
     *
     *
     *     c)act_ge_bytearray（资源文件表）
     *         存储流程定义相关的部署信息。即流程定义文档的存放地。每部署一次就会增加两条记录，    
     *     一条是关于bpmn规则文件的，一条是图片的（如果部署时只指定了bpmn一个文件，activiti    
     *     会在部署时解析bpmn文件内容自动生成流程图）。两个文件不是很大，都是以二进制形式存储在数据库中。
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
        System.out.println("流程激活ID: " + processInstance.getActivityId());
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
        String taskId = "10002";//上一次我们查询的任务ID就是304
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
     * 说明：
     * 1)deploymentId为流程部署ID
     * 2)resourceName为act_ge_bytearray表中NAME_列的值
     * 3)使用repositoryService的getDeploymentResourceNames方法可以获取指定部署下得所有文件的名称
     * 4)使用repositoryService的getResourceAsStream方法传入部署ID和资源图片名称可以获取部署下指定名称文件的输入流
     * 5)最后的有关IO流的操作，使用FileUtils工具的copyInputStreamToFile方法完成流程流程到文件的拷贝，
     * 将资源文件以流的形式输出到指定文件夹下。
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


    /**
     * 查看最新版本的流程定义
     */
    @Test
    public void queryAllLatestVersions() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        //先做一个升序排列
        List<ProcessDefinition> list = query.orderByProcessDefinitionVersion().asc() //使用版本升序排序
                .list();//获取流程定义对象List集合

        /**
         * Map<String,ProcessDefinition>
         * map集合的key：流程定义的key
         * map集合的value：流程定义的对象
         * map集合的特点：当map集合key值相同的情况下，后一次的值将替换前一次的值
         * */
        Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
        if (list != null && list.size() > 0) {
            for (ProcessDefinition pd : list) {
                map.put(pd.getKey(), pd);
            }
        }

        List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());
        //遍历集合，查看内容
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
     * 删除key相同的所有不同版本的流程定义
     * 我们的流程定义可能有多个版本，我们一般的删除是制定流程定义
     * 对象的id来进行删除的话，如果我们要彻底删除一个流程定义，那就
     * 要把该流程定义的所有版本全部删除。
     * <p>
     * <p>
     * eployment   部署对象
     * 1、一次部署的多个文件的信息。对于不需要的流程可以删除和修改。
     * 2、对应的表：
     *   act_re_deployment：部署对象表
     *   act_re_procdef：流程定义表
     *   act_ge_bytearray：资源文件表
     *   act_ge_property：主键生成策略表
     */
    @Test
    public void deleteProcessDefinitionByKey() {
        //流程定义的key
        String processDefinitionKey = "HelloWorld";
        //先使用流程定义的key查询流程定义，查询出所有的版本
        RepositoryService repositoryServic = activitiRule.getRepositoryService();
        ProcessDefinitionQuery query = repositoryServic.createProcessDefinitionQuery();
        List<ProcessDefinition> list = query.processDefinitionKey(processDefinitionKey).list();

        //遍历，获取每个流程定义的部署ID
        if (list != null && list.size() > 0) {
            for (ProcessDefinition pd : list) {
                //获取部署ID
                String deploymentId = pd.getDeploymentId();
                activitiRule.getRepositoryService()
                        .deleteDeployment(deploymentId, true);//级联删除
            }
        }
    }
}
