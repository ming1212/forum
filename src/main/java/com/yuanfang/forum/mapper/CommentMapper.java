package com.yuanfang.forum.mapper;

import com.yuanfang.forum.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    /**
     * 根据实体查询当前页所有的评论
     * @param entityType  实体的类型
     * @param entityId  实体的编号
     * @param offset  分页的起始行
     * @param limit  每页显示的数量
     * @return
     */
    List<Comment> getCommentsByEntity(int entityType, int entityId, int offset, int limit);


    /**
     * 根据实体的类型查询评论的数量
     * @param entityType  实体的类型
     * @param entityId  实体的编号
     * @return
     */
    int getCommentCountByEntity(int entityType, int entityId);

    /**
     * 添加一条评论
     * @param comment  评论
     * @return
     */
    int insertComment(Comment comment);

    /**
     * 根据评论的id查询一条评论
     * @param id
     * @return
     */
    Comment getCommentById(int id);

}
