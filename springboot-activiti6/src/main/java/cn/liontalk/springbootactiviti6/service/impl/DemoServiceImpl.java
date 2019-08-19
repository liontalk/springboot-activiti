package cn.liontalk.springbootactiviti6.service.impl;

import cn.liontalk.springbootactiviti6.dao.DemoDao;
import cn.liontalk.springbootactiviti6.entity.Demo;
import cn.liontalk.springbootactiviti6.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {


    @Autowired
    private DemoDao demoDao;

    @Override
    public Demo queryDemoById(int id) {
        return demoDao.queryDemoById(id);
    }
}
