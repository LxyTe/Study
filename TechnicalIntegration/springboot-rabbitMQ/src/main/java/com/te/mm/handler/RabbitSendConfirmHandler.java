package com.te.mm.handler;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 确认到达监听(生产者消息确认组件)
 * @author 刘新杨
 *   菩提本无树，
 *   明镜亦非台。
 */
@Service
public class RabbitSendConfirmHandler implements ConfirmCallback {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		// TODO Auto-generated method stub
	
		System.out.println("correlationData就是订单id:" + correlationData.getId());
        System.out.println("到达状态:"+ack);
        if(!ack){
        	/**
        	 * 此处应该搞个计数。超过3次就不发了，上定时任务
        	 */
        	Message message = correlationData.getReturnedMessage();
        	MessageProperties messageProperties =correlationData.getReturnedMessage().getMessageProperties();
        	System.out.println("消费发送失败异常处理");
        	rabbitTemplate.convertAndSend(messageProperties.getReceivedExchange(), messageProperties.getReceivedRoutingKey(), message,correlationData);
        	
        }
	}

}
