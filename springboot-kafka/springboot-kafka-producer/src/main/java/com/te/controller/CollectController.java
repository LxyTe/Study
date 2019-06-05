package com.te.controller;


import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.*;

import com.te.handler.KafkaSendResultHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/kafka")
public class CollectController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private KafkaSendResultHandler producerListener;

   
    @Test
    public void testProducerListen() throws InterruptedException {
    	//设置发送此消息的 时候使用监听器，使用了监听器，那么就需要使用线程睡眠(原因如下)
    	//否则发送时间较长的时候会导致进程提前关闭导致无法调用回调时间
    	//因为KafkaTemplate发送消息是采取异步方式发送的，
        kafkaTemplate.setProducerListener(producerListener);
        kafkaTemplate.send("topic.quick.demo", "test producer listen");
        Thread.sleep(1000);
    }
    @Test//如果你不想异步发送消息，那么可以使用下面的方法来同步发送
    //直接获取结果不进入等待
    public void syncSendMessage() throws InterruptedException, ExecutionException{
    	  kafkaTemplate.setProducerListener(producerListener);
    	  //.get()方法提供了两个参数   long timeout, TimeUnit unit
    	  //但是当发送的时间，大于超时时间的时候，就会报异常
          kafkaTemplate.send("topic.quick.demo", "test producer listen").get();
    }
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public void sendKafka(HttpServletRequest request, HttpServletResponse response) {
        try {
            String message = request.getParameter("message");
            logger.info("kafka的消息={}", message);
            //此处也可以发送到指定分区
            kafkaTemplate.send("test", "key", message);
            
            //发送带有时间戳的消息
//            kafkaTemplate.send("topic.quick.demo", 0, System.currentTimeMillis(), 0, "send message with timestamp");
//
//            //使用ProducerRecord发送消息(此处也可以指定topic，partition,key , value)
            //反正无论你传你什么参数，底层都是用ProducerRecord参数来进行发送的
            ProducerRecord record = new ProducerRecord("topic.quick.demo", "use ProducerRecord to send message");
//          ProducerRecord(String topic, Integer partition, K key, V value) {
            kafkaTemplate.send(record);
            
          //使用Message发送消息,这里自定义一些其他参数，发送的时候使用的是MessageHeaders
            Map map = new HashMap();
            map.put(KafkaHeaders.TOPIC, "topic.quick.demo");
            map.put(KafkaHeaders.PARTITION_ID, 0);
            map.put(KafkaHeaders.MESSAGE_KEY, 0);
            GenericMessage message3 = new GenericMessage("use Message to send message",new MessageHeaders(map));
            kafkaTemplate.send(message3);
           
            logger.info("发送kafka成功.");
            //return new Response(ResultCode.SUCCESS, "发送kafka成功", null);
        } catch (Exception e) {
            logger.error("发送kafka失败", e);
           // return new Response(ResultCode.EXCEPTION, "发送kafka失败", null);
        }
    }

}