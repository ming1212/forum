package com.yuanfang.forum.service;

import com.yuanfang.forum.mapper.DiscussPostMapper;
import com.yuanfang.forum.pojo.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> getDiscussPosts(int userId, int offset, int limit){
        return discussPostMapper.getDiscussPosts(userId, offset, limit);
    }

    public int getDiscussPostCount(int userId){
        return discussPostMapper.getDiscussPostCount(userId);
    }

}
