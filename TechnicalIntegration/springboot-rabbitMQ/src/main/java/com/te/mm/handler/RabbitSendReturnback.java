package com.te.mm.handler;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.stereotype.Service;
/**
 * 发送失败的时候会调用(这里的失败指的 没有交换机，或者交换机没有绑定，才会触发)
 * @author 刘新杨
 *   菩提本无树，
 *   明镜亦非台。
 */
@Service
public class RabbitSendReturnback implements ReturnCallback{

	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		// TODO Auto-generated method stub
		System.out.println("发送失败的消息:"+message);
		System.out.println("交换机信息:"+exchange);
		System.out.println("路由key信息:"+routingKey);
		System.out.println("失败的编码:"+replyCode);
		System.out.println("失败信息:"+replyText);
	}

}
