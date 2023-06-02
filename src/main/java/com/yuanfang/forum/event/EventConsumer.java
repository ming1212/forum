package com.yuanfang.forum.event;

import com.alibaba.fastjson.JSONObject;
import com.yuanfang.forum.pojo.Event;
import com.yuanfang.forum.pojo.Message;
import com.yuanfang.forum.service.MessageService;
import com.yuanfang.forum.utils.ForumConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer implements ForumConstant {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);  //记录日志

    @Autowired
    private MessageService messageService;  //用于向消息表插入数据

    @KafkaListener(topics = {TOPIE_COMMENT, TOPIE_LIKE, TOPIE_FOLLOW})
    public void handleCommentMessage(ConsumerRecord record){

        if(record == null || record.value() == null){
            logger.error("消息的内容为空！");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if(event == null){
            logger.error("消息格式错误！");
            return;
        }

        //发送站内通知
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());
        if(!event.getData().isEmpty()){
            for(Map.Entry<String, Object> entry : event.getData().entrySet()){
                content.put(entry.getKey(), entry.getValue());
            }
        }

        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);

    }

}
