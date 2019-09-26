package cn.liontalk.activitiqingjia;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ActivitiQingjiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiQingjiaApplication.class, args);
    }

}
