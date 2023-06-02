package com.yuanfang.forum.event;

import com.alibaba.fastjson.JSONObject;
import com.yuanfang.forum.pojo.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void fireEvent(Event event){   //处理事件

        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));  //将事件发布到指定的主题

    }

}
