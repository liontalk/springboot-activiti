package cn.liontalk.springbootactiviti6;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan("cn.liontalk.springbootactiviti6.dao")
public class SpringbootActiviti6Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootActiviti6Application.class, args);
    }

}
