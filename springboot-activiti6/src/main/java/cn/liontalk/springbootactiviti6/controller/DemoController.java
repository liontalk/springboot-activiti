package cn.liontalk.springbootactiviti6.controller;

import cn.liontalk.springbootactiviti6.entity.Demo;
import cn.liontalk.springbootactiviti6.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/get/{id}")
    public Demo getALibrarianInfo(@PathVariable("id") int id) {
        return demoService.queryDemoById(id);
    }
}
