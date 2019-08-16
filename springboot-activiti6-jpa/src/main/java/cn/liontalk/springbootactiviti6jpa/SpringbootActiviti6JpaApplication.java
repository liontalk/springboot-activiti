package cn.liontalk.springbootactiviti6jpa;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SpringbootActiviti6JpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootActiviti6JpaApplication.class, args);
    }

}
