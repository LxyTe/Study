package com.te.handler;

import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;

/**
 * 消费者异常处理器(和rabbitMQ中的一样)
 * @author Administrator
 *
 */
public class ConsumerAwareErrorHandler implements ConsumerAwareListenerErrorHandler{

	@Override
	public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
	
		//异常处理类,此类可以做重试策略，当消费者出现异常的时候发送给其它topic下的分区
		message.getHeaders();//可以得到你想要信息
		System.out.println(message.getPayload());
		return null;
	}

}
