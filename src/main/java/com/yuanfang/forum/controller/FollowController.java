package com.yuanfang.forum.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.yuanfang.forum.event.EventProducer;
import com.yuanfang.forum.pojo.Event;
import com.yuanfang.forum.pojo.Page;
import com.yuanfang.forum.pojo.User;
import com.yuanfang.forum.service.FollowService;
import com.yuanfang.forum.service.UserService;
import com.yuanfang.forum.utils.ForumConstant;
import com.yuanfang.forum.utils.ForumUtil;
import com.yuanfang.forum.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController implements ForumConstant{

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId){

        User user = hostHolder.getUser();
        followService.follow(user.getId(), entityId, entityType);

        //触发关注事件
        Event event = new Event()
                .setTopic(TOPIE_FOLLOW)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityId);
        eventProducer.fireEvent(event);

        return ForumUtil.getJSONString(0,"已关注！");

    }

    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId){

        User user = hostHolder.getUser();
        followService. unfollow(user.getId(), entityId, entityType);
        return ForumUtil.getJSONString(0,"已取消关注！");

    }

    @RequestMapping(path = "/followees/{userId}",method = RequestMethod.GET)
    public String getFollowee(@PathVariable("userId") int userId, Page page, Model model){

        User user = userService.getUserById(userId);
        if(user == null){
            throw  new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);

        //设置分页信息
        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows((int) followService.getFolloweeCount(userId, ForumConstant.ENTITY_TYPE_USER));
        List<Map<String, Object>> userList = followService.getFollowees(userId, page.getOffset(), page.getLimit());
        if(userList != null){
            for(Map<String, Object> map : userList){
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users",userList);

        return "/site/followee";

    }

    @RequestMapping(path = "/followers/{userId}",method = RequestMethod.GET)
    public String getFollower(@PathVariable("userId") int userId, Page page, Model model){

        User user = userService.getUserById(userId);
        if(user == null){
            throw  new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);

        //设置分页信息
        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows((int) followService.getFollowerCount(userId, ForumConstant.ENTITY_TYPE_USER));
        List<Map<String, Object>> userList = followService.getFollowers(userId, page.getOffset(), page.getLimit());
        if(userList != null){
            for(Map<String, Object> map : userList){
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users",userList);

        return "/site/follower";

    }

    private boolean hasFollowed(int userId){
        if(hostHolder.getUser() == null){
            return false;  //当前用户没有登录直接返回false 表示未关注
        }

        return followService.hasFollowed(hostHolder.getUser().getId(), ForumConstant.ENTITY_TYPE_USER, userId);//返回当前登录的用户是否已经关注该用户
    }

}
