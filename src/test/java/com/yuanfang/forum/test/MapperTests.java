package com.yuanfang.forum.test;

import com.yuanfang.forum.mapper.DiscussPostMapper;
import com.yuanfang.forum.mapper.UserMapper;
import com.yuanfang.forum.pojo.DiscussPost;
import com.yuanfang.forum.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class MapperTests {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testGetDiscussPosts(){

        List<DiscussPost> list = discussPostMapper.getDiscussPosts(111, 0, 10);
        list.forEach(System.out::println);

        int count = discussPostMapper.getDiscussPostCount(111);
        System.out.println(count);

    }

    @Test
    public void testGetUserById(){
        User user = userMapper.getUserById(154);
        System.out.println(user);
    }
    @Test
    public void testGetUserByEmail(){
        User user = userMapper.getUserByEmail("2537599541@qq.com");
        System.out.println(user);
    }
    @Test
    public void testGetUserByUsername(){
        User user = userMapper.getUserByUsername("yuanfang");
        System.out.println(user);
    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("xiaonian");
        user.setPassword("xiaonian");
        user.setSalt("xiaonian");
        user.setType(0);
        user.setStatus(0);
        user.setEmail("xiaonian@qq.com");
        user.setActivationCode("xiaonian");
        user.setHeaderUrl("xiaonian");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        System.out.println(user);
    }

    @Test
    public void testUpdateStatus(){
        userMapper.updateStatus(155,1);
    }

    @Test
    public void testUpdateHeader(){
        userMapper.updateHeader(155,"yuanfang");
    }

    @Test
    public void testUpdatePassword(){
        userMapper.updatePassword(155,"yuanfang");
    }

}
