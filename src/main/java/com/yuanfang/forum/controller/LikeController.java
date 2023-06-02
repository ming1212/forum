package com.yuanfang.forum.controller;

import com.yuanfang.forum.event.EventProducer;
import com.yuanfang.forum.pojo.Event;
import com.yuanfang.forum.pojo.User;
import com.yuanfang.forum.service.LikeService;
import com.yuanfang.forum.utils.ForumConstant;
import com.yuanfang.forum.utils.ForumUtil;
import com.yuanfang.forum.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController implements ForumConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId, int postId){
        User user = hostHolder.getUser();  //当前用户
        //点赞操作
        likeService.like(user.getId(), entityType, entityId, entityUserId);
        //点赞的数量
        long likeCount = likeService.getEntityLikeCount(entityType, entityId);
        //点赞的状态
        int likeStatus = likeService.getEntityLikeStatus(user.getId(), entityType, entityId);
        //返回结果
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount",likeCount);
        map.put("likeStatus",likeStatus);

        //触发点赞事件
        if(likeStatus == 1){  //是否是点赞操作而不是取消点赞的操作，取消点赞不需要触发事件
            Event event = new Event()
                    .setTopic(TOPIE_LIKE)
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setUserId(hostHolder.getUser().getId())
                    .setData("postId", postId);
            eventProducer.fireEvent(event);
        }

        return ForumUtil.getJSONString(0, null, map);


    }



}
