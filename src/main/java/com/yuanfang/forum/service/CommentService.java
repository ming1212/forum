package com.yuanfang.forum.service;

import com.yuanfang.forum.mapper.CommentMapper;
import com.yuanfang.forum.pojo.Comment;
import com.yuanfang.forum.utils.ForumConstant;
import com.yuanfang.forum.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService implements ForumConstant {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;

    public List<Comment> getCommentsByEntity(int entityType, int entityId, int offset, int limit){
        return commentMapper.getCommentsByEntity(entityType, entityId, offset, limit);
    }

    public int getCommentCountByEntity(int entityType, int entityId){
        return commentMapper.getCommentCountByEntity(entityType, entityId);
    }

    //涉及到两个DML操作：添加一条评论，然后修改帖子的评论的数量，所以加上事务
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED) //设置事务隔离级别以及事务的传播行为
    public int addComment(Comment comment){

        if(comment == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));  //设置评论的内容
        comment.setContent(sensitiveFilter.filter(comment.getContent()));  //敏感词过滤
        int rows = commentMapper.insertComment(comment);

        //更新帖子的评论数量，判断是否是对帖子进行的评论
        if(comment.getEntityType() == ENTITY_TYPE_POST){   //评论的实体类型是否是帖子
            int count = commentMapper.getCommentCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(),count);
        }

        return rows;
    }

    public Comment getCommentById(int id){
        return commentMapper.getCommentById(id);
    }

}
