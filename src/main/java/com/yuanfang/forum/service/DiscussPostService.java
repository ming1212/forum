package com.yuanfang.forum.service;

import com.yuanfang.forum.mapper.DiscussPostMapper;
import com.yuanfang.forum.pojo.DiscussPost;
import com.yuanfang.forum.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> getDiscussPosts(int userId, int offset, int limit){
        return discussPostMapper.getDiscussPosts(userId, offset, limit);
    }

    public int getDiscussPostCount(int userId){
        return discussPostMapper.getDiscussPostCount(userId);
    }


    public int addDiscussPost(DiscussPost post){
        if(post == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //转义HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        //过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));
        return discussPostMapper.insertDiscussPost(post);
    }

    public DiscussPost getDiscussPostById(int id){
        return discussPostMapper.getDiscussPostById(id);
    }

    public int updateCommentCount(int id, int commentCount){
        return discussPostMapper.updateCommentCount(id, commentCount);
    }

}
