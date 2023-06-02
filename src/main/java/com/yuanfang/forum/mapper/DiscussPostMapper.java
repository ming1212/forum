package com.yuanfang.forum.mapper;

import com.yuanfang.forum.pojo.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    /**
     * 查询所有的帖子，传入userId那么就是查询该用户发布的所有的帖子。
     * @param userId  指定是哪个用户,查询时采用动态sql，传入就是查询每一个用户的发布帖子数量，不传入就是查询所有的帖子数量
     * @param offset  分页显示时的帖子的起始id
     * @param limit   每页显示的帖子的数量
     * @return
     */
    List<DiscussPost> getDiscussPosts(int userId, int offset, int limit);

    /**
     * 获取所有帖子的数量
     * @param userId  传入用户的id，那么就查询当前用户的所有的帖子的数量，不传入就查询所有的帖子的数量
     * @return
     */
    // @Param注解用于给参数取别名
    // 如果只有一个参数，并且需要在动态sql中<if>使用，则必须加别名
    int getDiscussPostCount(@Param("userId") int userId);

    /**
     * 插入一条新的帖子
     * @param discussPost
     * @return
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 根据帖子的id查询帖子的信息
     * @param id
     * @return
     */
    DiscussPost getDiscussPostById(int id);

    /**
     * 更新帖子的评论数量
     * @param id  帖子的id
     * @param commentCount  帖子的评论的数量
     * @return
     */
    int updateCommentCount(int id, int commentCount);

}
