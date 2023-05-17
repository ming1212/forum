package com.yuanfang.forum.test;

import com.yuanfang.forum.utils.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SensitiveTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter(){

        String text = "这是一个可以赌博，吸毒的场所。当然不可以强奸美女，也不可以嫖娼。";
        String filter = sensitiveFilter.filter(text);
        System.out.println(filter);

        text = "这是一个可以🤍赌🤍博🤍，🤍吸🤍毒🤍的场所。当然不可以🤍强-奸🤍美女，也不可以🤍嫖🤍娼🤍。";
        filter = sensitiveFilter.filter(text);
        System.out.println(filter);
    }

}
