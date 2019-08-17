package cn.liontalk.springbootactiviti6jpa.coreapi.runtime;

import cn.liontalk.springbootactiviti6jpa.SpringbootActiviti6JpaApplicationTests;
import cn.liontalk.springbootactiviti6jpa.coreapi.repository.TestRepository;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.assertj.core.util.Maps;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RunTimeServiceTest extends SpringbootActiviti6JpaApplicationTests {


    public static final Logger log = LoggerFactory.getLogger(TestRepository.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("processes/activiti.cfg.xml");


    //根据key 去启动
    @Test
    @Deployment(resources = {"processes/test.bpmn20.xml"})
    public void startProcess() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("demo2", map);
        log.info("processInstance == {}", processInstance);
    }


    @Test
    @Deployment(resources = {"processes/test.bpmn20.xml"})
    public void startProcessById() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessDefinition definition = activitiRule.getRepositoryService().createProcessDefinitionQuery().singleResult();
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(definition.getId(), map);
        log.info("processInstance == {}", processInstance);
    }

}
