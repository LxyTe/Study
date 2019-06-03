package com.te.mm.handler;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

@Service(value="MessagePostProcessorConfig")
public class MessagePostProcessorConfig implements MessagePostProcessor{

	@Override
	public Message postProcessMessage(Message message) throws AmqpException {
	
		//此处可设置超时时间和优先级 
		// message.getMessageProperties().setExpiration("3000" );//10秒后超时
		/**
		 * 下面的设置方法也可以实现 延时消费
		 */
		 message.getMessageProperties().setHeader("x-delay",3000);
		 return message;
	}

}
