package com.te.mm.handler;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.stereotype.Service;
/***
 * 消费者异常处理器()
 * 有时候我们使用的时候，可能是一个异常类对应一个死信队列
 * @author 刘新杨
 *   菩提本无树，
 *   明镜亦非台。
 */
@Service(value="RabbitConsumerListenerErrorHandler")//使用的时候可以直接在@rabbitMQ中errorHandler设置名字即可
public class RabbitConsumerListenerErrorHandler implements RabbitListenerErrorHandler {

	@Override
	public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message,
			ListenerExecutionFailedException exception) throws Exception {
	
		System.out.println("消费失败的异常是:"+amqpMessage.getMessageProperties().getConsumerQueue());
		return "此处应该做消费重新消费，或者发送到死信交换机";
	}

}
