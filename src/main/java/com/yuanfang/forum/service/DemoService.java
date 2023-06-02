package com.yuanfang.forum.service;

import com.yuanfang.forum.mapper.DiscussPostMapper;
import com.yuanfang.forum.mapper.UserMapper;
import com.yuanfang.forum.pojo.DiscussPost;
import com.yuanfang.forum.pojo.User;
import com.yuanfang.forum.utils.ForumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

@Service
public class DemoService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    //模拟事务
    //两个dml操作

    /**
     * isolation:设置事务的隔离级别
     * propagation:设置事务的传播行为
     *      REQUIRED: 支持当前事务（A方法支持A事务，B方法支持B事务，A调用B，支持A事务）
     *      REQUIRES_NEW: 创建一个新事务（A方法支持A事务，B方法支持B事务，A调用B，支持B事务）
     *      NESTED: 嵌套在当前事务中执行（独立的提交和回滚）
     * Transactional：声明式事务
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String demo1(){

        //先增加用户
        User user = new User();
        user.setUsername("yuanfang");
        user.setPassword("yuanfang");
        user.setHeaderUrl("yuanfang");
        user.setSalt(ForumUtil.generateUUID().substring(0,5));
        user.setActivationCode(ForumUtil.generateUUID());
        user.setEmail("yuanfang@qq.com");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //再增加帖子
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle("yuanfang");
        discussPost.setContent("yuanfang");
        discussPost.setCreateTime(new Date());

        System.out.println(10 / 0);
        return "ok";

    }


    /**
     * 编程式事务（通过TransactionTemplate实现）
     * @return
     */
    public String demo2(){

        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);  //设置事务的隔离级别
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);  //设置事务的传播行为

        return transactionTemplate.execute(new TransactionCallback<String>() {
            @Override
            public String doInTransaction(TransactionStatus status) {

                //先增加用户
                User user = new User();
                user.setUsername("xiaonian");
                user.setPassword("xiaonian");
                user.setHeaderUrl("xiaonian");
                user.setSalt(ForumUtil.generateUUID().substring(0,5));
                user.setActivationCode(ForumUtil.generateUUID());
                user.setEmail("xiaonian@qq.com");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);

                //再增加帖子
                DiscussPost discussPost = new DiscussPost();
                discussPost.setUserId(user.getId());
                discussPost.setTitle("xiaonian");
                discussPost.setContent("xiaonian");
                discussPost.setCreateTime(new Date());

                System.out.println(10 / 0);
                return "ok";

            }
        });

    }

}
