package com.yuanfang.forum.controller;

import com.yuanfang.forum.pojo.DiscussPost;
import com.yuanfang.forum.pojo.Page;
import com.yuanfang.forum.pojo.User;
import com.yuanfang.forum.service.DiscussPostService;
import com.yuanfang.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeComtroller {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){  //page接收页面传来的跟分页相关的数据

        page.setRows(discussPostService.getDiscussPostCount(0));  //设置帖子的总的条数
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.getDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if(list != null){
            for(DiscussPost discussPost : list){
                Map<String, Object> map = new HashMap<>();
                map.put("discussPost", discussPost);
                User user = userService.getUserById(discussPost.getUserId());
                map.put("user",user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "/index";
    }

}
