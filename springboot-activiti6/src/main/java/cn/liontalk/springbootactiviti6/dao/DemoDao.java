package cn.liontalk.springbootactiviti6.dao;

import cn.liontalk.springbootactiviti6.entity.Demo;
import org.apache.ibatis.annotations.Param;

public interface DemoDao {

    Demo queryDemoById(@Param("id") int id);

}
