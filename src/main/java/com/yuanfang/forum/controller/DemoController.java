package com.yuanfang.forum.controller;

import com.yuanfang.forum.utils.ForumUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//@RequestMapping("/demo")
public class DemoController {

    @RequestMapping(path = "/ajax", method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String username, int age){

        System.out.println(username);
        System.out.println(age);
        return ForumUtil.getJSONString(0,"操作成功");

    }

}
