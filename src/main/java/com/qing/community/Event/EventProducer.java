package com.qing.community.Event;

import com.alibaba.fastjson.JSONObject;
import com.qing.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void fireEvent(Event event) {
        //将事件通过json字符串发送到对应的主题
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}
