package cn.liontalk;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan("cn.liontalk.dao")
public class SpringbootActivitiApplication {

    private final static Logger logger = LoggerFactory.getLogger(SpringbootActivitiApplication.class);

    public static void main(String[] args) {
        logger.info("项目启动。。。。");
        SpringApplication.run(SpringbootActivitiApplication.class, args);
    }

}
