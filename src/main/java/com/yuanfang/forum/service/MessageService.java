package com.yuanfang.forum.service;

import com.yuanfang.forum.mapper.MessageMapper;
import com.yuanfang.forum.pojo.Message;
import com.yuanfang.forum.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Message> getConversations(int userId, int offset, int limit){
        return messageMapper.getConversations(userId,offset,limit);
    }

    public int getConversationCount(int userId){
        return messageMapper.getConversationCount(userId);
    }

    public List<Message> getLetters(String conversationId, int offset, int limit){
        return messageMapper.getLetters(conversationId, offset, limit);
    }

    public int getLetterCount(String conversationId){
        return messageMapper.getLetterCount(conversationId);
    }

    public int getUnreadLetterCount(int userId, String conversationId){
        return messageMapper.getUnreadLetterCount(userId,conversationId);
    }

    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));  //过滤标签
        message.setContent(sensitiveFilter.filter(message.getContent()));  //过滤敏感词
        return messageMapper.insertMessage(message);
    }

    public int readMessage(List<Integer> ids){
        return messageMapper.updateStatus(ids, 1);
    }

    public Message getLatestNotice(int userId, String topic){
        return messageMapper.getLatestNotice(userId, topic);
    }

    public int getNoticeCount(int userId, String topic){
        return messageMapper.getNoticeCount(userId, topic);
    }

    public int getNoticeUnreadCount(int userId, String topic){
        return messageMapper.getNoticeUnreadCount(userId, topic);
    }

    public List<Message> getNotices(int userId, String topic, int offset, int limit){
        return messageMapper.getNotices(userId, topic, offset, limit);
    }

}
