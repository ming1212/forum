package com.yuanfang.forum.test;

import com.yuanfang.forum.mapper.DiscussPostMapper;
import com.yuanfang.forum.mapper.LoginTicketMapper;
import com.yuanfang.forum.mapper.MessageMapper;
import com.yuanfang.forum.mapper.UserMapper;
import com.yuanfang.forum.pojo.DiscussPost;
import com.yuanfang.forum.pojo.LoginTicket;
import com.yuanfang.forum.pojo.Message;
import com.yuanfang.forum.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class MapperTests {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;

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

    @Test
    public void testStack(){

        Stack<Integer> stack = new Stack<>();
        Queue<Integer> queue = new ArrayDeque<>();
        Queue<Integer> q = new LinkedList<>();
        q.isEmpty();


    }


    @Test
    public void testInsertLoginTicket(){

        LoginTicket loginTicket =
                new LoginTicket(111,"yuanfang",0,new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        loginTicketMapper.insertLoginTicket(loginTicket);

    }

    @Test
    public void testGetLoginTicket(){

        LoginTicket loginTicket = loginTicketMapper.getLoginTicket("yuanfang");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("yuanfang",1);
        loginTicket = loginTicketMapper.getLoginTicket("yuanfang");
        System.out.println(loginTicket);

    }


    @Test
    public void testGetLetters(){

        List<Message> conversations = messageMapper.getConversations(111, 0, 20);
        conversations.forEach(System.out::println);//遍历输出每一个会话中的最新的一条消息

        int count = messageMapper.getConversationCount(111);
        System.out.println(count);

        List<Message> letters = messageMapper.getLetters("111_112", 0, 10);
        letters.forEach(System.out::println);

        int letterCount = messageMapper.getLetterCount("111_112");
        System.out.println(letterCount);

        int unreadLetterCount = messageMapper.getUnreadLetterCount(131, "111_131");
        System.out.println(unreadLetterCount);

    }

}
