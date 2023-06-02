package com.yuanfang.forum.test;

import com.yuanfang.forum.service.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DemoTests {

    @Autowired
    private DemoService demoService;

    @Test
    public void testDemoTest(){
        String result = demoService.demo1();
        System.out.println(result);
    }


    @Test
    public void testDemo2Test(){
        String result = demoService.demo2();
        System.out.println(result);
    }

}
