package com.yuanfang.forum.test;

import com.yuanfang.forum.utils.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testMail01(){
        mailClient.sendMail("yuanfang4828@163.com","测试","测试邮件");
    }

    @Test
    public void testMail02(){
        mailClient.sendMail("2537599541@qq.com","测试","测试邮件");
    }

    @Test
    public void testMail03(){
        Context context = new Context();
        context.setVariable("username","xiaonian");
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);
        mailClient.sendMail("yuanfang4828@163.com","测试邮件",content);
    }

}
