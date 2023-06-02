package com.yuanfang.forum.service;

import com.yuanfang.forum.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    //点赞操作  用set集合实现，集合中存的是点赞用户的id
    public void like(int userId, int entityType, int entityId, int entityUserId){
//        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, enetityId);
//        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);   //存的是用户的id
//        if(isMember){   //是否点赞过
//            redisTemplate.opsForSet().remove(entityLikeKey, userId);   //点赞过就取消点赞
//        }else{
//            redisTemplate.opsForSet().add(entityLikeKey, userId);  //没点赞过就点赞
//        }
        //事务
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

                Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);

                operations.multi();

                if(isMember){  //如果已经点赞过
                    redisTemplate.opsForSet().remove(entityLikeKey,userId);
                    redisTemplate.opsForValue().decrement(userLikeKey);
                }else{
                    redisTemplate.opsForSet().add(entityLikeKey,userId);
                    redisTemplate.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });

    }

    //查询某实体点赞的数量
    public long getEntityLikeCount(int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询某人对某实体的点赞状态  (点赞状态用0、1、2...表示，方便扩展功能，比如踩操作，而不是用true和false)
    public int getEntityLikeStatus(int userId, int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId) ? 1 : 0;
    }

    //查询某个用户获得的赞的数量
    public int getUserLikeCount(int userId){
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count.intValue();
    }

}
