package com.yuanfang.forum.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisString(){  //测试字符串

        String redisKey = "test:count";
        redisTemplate.opsForValue().set(redisKey,1);

        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));


    }

    @Test
    public void testRedisHashs(){   //测试哈希表

        String redisKey = "test:user";
        redisTemplate.opsForHash().put(redisKey,"id", 23);
        redisTemplate.opsForHash().put(redisKey,"username", "yuanfang");
        System.out.println(redisTemplate.opsForHash().get(redisKey,"username"));
        System.out.println(redisTemplate.opsForHash().get(redisKey,"id"));

    }

    @Test
    public void testRedisList(){   //测试列表

        String redisKey = "test:students";
        redisTemplate.opsForList().leftPush(redisKey,"zhangfei");
        redisTemplate.opsForList().leftPush(redisKey,"wangzhaojun");
        redisTemplate.opsForList().leftPush(redisKey,"libai");

        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(redisTemplate.opsForList().index(redisKey,0));
        System.out.println(redisTemplate.opsForList().range(redisKey,0,1));

        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));

    }


    @Test
    public void testRedisSet(){   //测试无序集合

        String redisKey = "test:teacher";
        redisTemplate.opsForSet().add(redisKey,"鲁班","狄仁杰","阿里","虞姬","百里");

        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
        System.out.println(redisTemplate.opsForSet().members(redisKey));

    }


    @Test
    public void testRedisSortedSet(){  //测试有序集合

        String redisKey = "test:vip";
        redisTemplate.opsForZSet().add(redisKey,"悟空",100);
        redisTemplate.opsForZSet().add(redisKey,"八戒",80);
        redisTemplate.opsForZSet().add(redisKey,"沙僧",50);
        redisTemplate.opsForZSet().add(redisKey,"唐僧",80);
        redisTemplate.opsForZSet().add(redisKey,"马儿",90);

        System.out.println(redisTemplate.opsForZSet().size(redisKey));
        System.out.println(redisTemplate.opsForZSet().range(redisKey,0,2));
        System.out.println(redisTemplate.opsForZSet().reverseRange(redisKey,0,2));
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey,"悟空"));
        System.out.println(redisTemplate.opsForZSet().score(redisKey,"八戒"));

    }

    @Test
    public void testKeys(){

        redisTemplate.delete("test:user");
        System.out.println(redisTemplate.hasKey("test:user"));
        redisTemplate.expire("test:vip",20, TimeUnit.SECONDS);

    }


    @Test
    public void testBoundOperations(){

        String redisKey = "test:count";
        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        System.out.println(operations.get());

        String setKey = "test:teacher";
        BoundSetOperations setOperations = redisTemplate.boundSetOps(setKey);
        setOperations.add("张三","李四","王五","赵六");
        System.out.println(setOperations.size());
        System.out.println(setOperations.members());

    }

    @Test
    public void testTransactional(){
        Object obj = redisTemplate.execute(new SessionCallback() {  //匿名内部类
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:tm";

                operations.multi();  //开启事务

                operations.opsForSet().add(redisKey,"zhangsan");
                operations.opsForSet().add(redisKey,"lisi");
                operations.opsForSet().add(redisKey,"wangwu");

                System.out.println(operations.opsForSet().members(redisKey));  //事务中的查询操作无效

                return operations.exec(); //提交事务

            }
        });
        System.out.println(obj);
    }


}
