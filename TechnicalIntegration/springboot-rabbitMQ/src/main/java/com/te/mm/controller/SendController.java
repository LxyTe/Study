package com.te.mm.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.mm.handler.MessagePostProcessorConfig;
import com.te.mm.handler.RabbitSendConfirmHandler;
import com.te.mm.handler.RabbitSendReturnback;
import com.te.mm.producer.DeadQueue;
import com.te.mm.producer.MainConfig;
import com.te.uuid.UniqueOrderGenerate;

//@RestController
@Component
public class SendController {

	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired //支持可靠性投递
	private RabbitSendConfirmHandler  rabbitSendConfirmHandler;
	
	@Autowired
	private RabbitSendReturnback  rabbitSendReturnback;
	
	@Resource(name = "defauilTemplate")
	private RabbitTemplate defauilTemplate;
	 
	@Autowired//消息扩展器
	private MessagePostProcessorConfig messagePostProcessorConfig;
	

//	@RequestMapping("/send")
	 public void sendDirect(Object message , Map<String, Object> properties) throws Exception{
			
		 //设置请求头等属性
		 MessageHeaders mhs = new MessageHeaders(properties);
		 //创建消息的核心内容
	    Message mes = MessageBuilder.createMessage(message, mhs);
	   
	    /**
	     *  这样可以支持消息的确认模式和返回模式
	     */
       
	    rabbitTemplate.setConfirmCallback(rabbitSendConfirmHandler); 
	    rabbitTemplate.setReturnCallback(rabbitSendReturnback);
	    CorrelationData correlationData = new CorrelationData(properties.get("number").toString());
	    rabbitTemplate.convertAndSend(MainConfig.Test_Direct_Change,MainConfig.Test_Direct_ROUTING_KEY,mes , correlationData);
	 }
	 
	 public void sendTopic(Object message , Map<String, Object> properties) throws Exception{
			
		 //设置请求头等属性
		 MessageHeaders mhs = new MessageHeaders(properties);
		 //创建消息的核心内容
	    Message mes = MessageBuilder.createMessage(message, mhs);
	   
   
	    /**
	     *  这样可以支持消息的确认模式和返回模式
	     */
       
	    rabbitTemplate.setConfirmCallback(rabbitSendConfirmHandler); 
	    rabbitTemplate.setReturnCallback(rabbitSendReturnback);
	    CorrelationData correlationData = new CorrelationData(properties.get("number").toString());	    
	    rabbitTemplate.convertAndSend(DeadQueue.deadExchangeName,DeadQueue.Test_Topic_ROUTING_KEY,mes,messagePostProcessorConfig,correlationData);
	 }
	 
	 public void sendDelayedDirect(Object message , Map<String, Object> properties) throws Exception{
			
		 //设置请求头等属性
		 MessageHeaders mhs = new MessageHeaders(properties);
		 //创建消息的核心内容
	    Message mes = MessageBuilder.createMessage(message, mhs);
	   
   
	    /**
	     *  这样可以支持消息的确认模式和返回模式
	     */
       
	    rabbitTemplate.setConfirmCallback(rabbitSendConfirmHandler); 
	    rabbitTemplate.setReturnCallback(rabbitSendReturnback);
	    CorrelationData correlationData = new CorrelationData(properties.get("number").toString());	    
	    rabbitTemplate.convertAndSend("test_exchange","test_queue_1",mes,messagePostProcessorConfig,correlationData);
	 }
	 public void sendFanout(Object message , Map<String, Object> properties) throws Exception{
			
		 //设置请求头等属性
		 MessageHeaders mhs = new MessageHeaders(properties);
		 //创建消息的核心内容
	    Message mes = MessageBuilder.createMessage(message, mhs);
	   
	    /**
	     *  这样可以支持消息的确认模式和返回模式
	     */
       
	    rabbitTemplate.setConfirmCallback(rabbitSendConfirmHandler); 
	    rabbitTemplate.setReturnCallback(rabbitSendReturnback);
	    CorrelationData correlationData = new CorrelationData(properties.get("number").toString());
	    rabbitTemplate.convertAndSend(MainConfig.Test_Fanout_Change,"",mes , correlationData);
	 }
	 
	 //支持快速发送
	 public void sendFastsending(Object message , Map<String, Object> properties) throws Exception{
			
		 //设置请求头等属性
		 MessageHeaders mhs = new MessageHeaders(properties);
		 //创建消息的核心内容
	    Message mes = MessageBuilder.createMessage(message, mhs);
	   
	    /**
	     *  这样可以支持消息的确认模式和返回模式
	     */
       
	    defauilTemplate.setConfirmCallback(rabbitSendConfirmHandler); 
	    defauilTemplate.setReturnCallback(rabbitSendReturnback);
	    CorrelationData correlationData = new CorrelationData(properties.get("number").toString());
	   //默认的交换机，默认的key。已经在默认模板中声明了队列
	    defauilTemplate.correlationConvertAndSend(mes,correlationData);
	 }
}
