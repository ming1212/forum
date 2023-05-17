package com.yuanfang.forum.service;

import com.yuanfang.forum.mapper.LoginTicketMapper;
import com.yuanfang.forum.mapper.UserMapper;
import com.yuanfang.forum.pojo.LoginTicket;
import com.yuanfang.forum.pojo.User;
import com.yuanfang.forum.utils.ForumConstant;
import com.yuanfang.forum.utils.ForumUtil;
import com.yuanfang.forum.utils.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements ForumConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${forum.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User getUserById(int id){
        return userMapper.getUserById(id);
    }

    public Map<String,Object> register(User user){
        Map<String, Object> map = new HashMap<>();

        //空值的判断
        if(user == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","用户名不能为空！");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空！");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空！");
            return map;
        }

        //验证账号
        User u = userMapper.getUserByUsername(user.getUsername());
        if(u != null){
            map.put("usernameMsg","该账号已存在");
            return map;
        }
        //验证邮箱
        u = userMapper.getUserByEmail(user.getEmail());
        if(u != null){
            map.put("emailMsg","该邮箱已被注册！");
            return map;
        }

        //注册用户
        user.setSalt(ForumUtil.generateUUID().substring(0,5));
        user.setPassword(ForumUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(ForumUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        // http://localhost:8080/forum/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(),"激活邮件",content);

        return map;
    }

    public int activation(int userId, String code){
        User user = userMapper.getUserById(userId);
        if(user.getStatus() == 1){
            return ACTIVATION_REPEATED;
        }else if(user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }else{
            return ACTIVATION_UNSUCCESS;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds){
        Map<String, Object> map = new HashMap<>();

        //空值的判断
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空！");
            return map;
        }

        //验证账号
        User user = userMapper.getUserByUsername(username);
        if(user == null){
            map.put("usernameMsg","该账号不存在！");
            return map;
        }

        //验证状态
        if(user.getStatus() == 0){
            map.put("usernameMsg","该账号尚未激活！");
            return map;
        }

        //验证密码
        password = ForumUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg","密码不正确！");
            return map;
        }

        //以上都通过说明登录成功，然后生成登录凭证
//        LoginTicket loginTicket =
//                new LoginTicket(user.getId(),ForumUtil.generateUUID(),0,new Date(System.currentTimeMillis() + expiredSeconds * 1000));
//            loginTicketMapper.insertLoginTicket(loginTicket);
//        map.put("ticket",loginTicket.getTicket());
//
//        return map;
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(ForumUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);
        map.put("ticket",loginTicket.getTicket());

        return map;
    }

    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }

    public LoginTicket getLoginTicket(String ticket){
        return loginTicketMapper.getLoginTicket(ticket);
    }

    public int updateHeader(int userId, String headerUrl){
        return userMapper.updateHeader(userId,headerUrl);
    }



}
