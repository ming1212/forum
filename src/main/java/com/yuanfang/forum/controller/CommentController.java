package com.yuanfang.forum.controller;

import com.yuanfang.forum.event.EventProducer;
import com.yuanfang.forum.pojo.Comment;
import com.yuanfang.forum.pojo.DiscussPost;
import com.yuanfang.forum.pojo.Event;
import com.yuanfang.forum.service.CommentService;
import com.yuanfang.forum.service.DiscussPostService;
import com.yuanfang.forum.utils.ForumConstant;
import com.yuanfang.forum.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements ForumConstant {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiscussPostService discussPostService;

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment){

        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        //评论完之后，出发评论事件，给被评论的人发消息
        Event event = new Event()
                .setTopic(TOPIE_COMMENT)
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setUserId(hostHolder.getUser().getId())
                .setData("postId", discussPostId);
        if(comment.getEntityType() == ENTITY_TYPE_POST){   //评论的帖子
            DiscussPost target = discussPostService.getDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }else if(comment.getEntityType() == ENTITY_TYPE_COMMENT){
            Comment target = commentService.getCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }

        eventProducer.fireEvent(event);

        return "redirect:/discuss/detail/" + discussPostId;

    }

}
