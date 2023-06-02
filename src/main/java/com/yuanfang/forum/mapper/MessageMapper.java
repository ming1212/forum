package com.yuanfang.forum.mapper;

import com.yuanfang.forum.pojo.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    //查询当前用户的会话列表，针对每个会话只返回一条最新的私信
    List<Message> getConversations(int userId, int offset, int limit);

    //查询当前用户的会话数量
    int getConversationCount(int userId);

    //查询某个会话所包含的私信列表
    List<Message> getLetters(String conversationId, int offset, int limit);

    //查询某个会话所包含的私信数量
    int getLetterCount(String conversationId);

    //查询未读的私信数量
    int getUnreadLetterCount(int userId, String conversationId);


    //新增消息
    int insertMessage(Message message);

    //修改消息的状态（一下可以读很多的消息，所以需要修改状态的消息是一个集合）
    int updateStatus(List<Integer> ids, int status);

    //查询某个主题下最新的通知
    Message getLatestNotice(int userId, String topic);

    //查询某个主题下所包含的通知的数量
    int getNoticeCount(int userId, String topic);

    //查询某个主题下未读的通知的数量
    int getNoticeUnreadCount(int userId, String topic);

    //查询某个主题所包含的通知列表,支持分页
    List<Message> getNotices(int userId, String topic, int offset, int limit);


}
