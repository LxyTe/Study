package com.te.mm.listener;

import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.te.mm.Entity.Order;

/**
 * 正常情况下消费者队列应该在其他项目中，这里只做演示所以就放在了在这里
 * 
 * @author Administrator
 *
 */
@Component
public class RabbitReceiver {

	// SimpleRabbitListenerContainerFactory此类可以自定义一些配置详细请点击

	// @RabbitListener(bindings = @QueueBinding(value = @Queue(value =
	// "${spring.rabbitmq.listener.order.queue.name}", durable =
	// "${spring.rabbitmq.listener.order.queue.durable}"), exchange =
	// @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}", durable
	// = "${spring.rabbitmq.listener.order.exchange.durable}", type =
	// "${spring.rabbitmq.listener.order.exchange.type}",
	// ignoreDeclarationExceptions =
	// "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),
	// key = "${spring.rabbitmq.listener.order.key}"),containerFactory="tt")
	// @RabbitHandler
	// public void onMessage(Message message, Channel channel) throws Exception {
	//
	//
	// System.out.println("*******");
	// System.out.println("消息体内容:" + message.getPayload());
	// long deTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
	// channel.basicAck(deTag, false);
	// }

	@RabbitListener(containerFactory = "tt", errorHandler = "RabbitConsumerListenerErrorHandler", queues = "TestDirectQueue")
	@RabbitHandler // 此注解加上之后可以接受对象型消息
	public void onOrderMessage(@Payload List<Order> orders, Channel channel, @Headers Map<String, Object> heads)
			throws Exception {

		for (Order order : orders) {
			System.out.println(order);
		}
		// 此处也可以带messagid等信息作为唯一标识来确保消息的幂等操作
		System.out.println("消息唯一标识符:" + heads.get("number"));
		/**
		 * 下面的是设置为手动确认消费成功 1.需要在配置文件中开启手动消费 2.下面代码 确认消费的意思是：当下面两行代码走完后，那么此消息就会从服务器中删除。
		 */
		long deTag = (Long) heads.get(AmqpHeaders.DELIVERY_TAG);
		System.out.println(deTag);
		// 告诉服务器，已经消费成功,
		channel.basicAck(deTag, false);
	}

	@RabbitListener(containerFactory="tt", errorHandler = "RabbitConsumerListenerErrorHandler", queues = "dead_queue")
	@RabbitHandler
	public void onDeadMessage2(Message message, Channel channel, @Headers Map<String, Object> heads) throws Exception {

		System.out.println("死信队列message"+new String(message.getBody()));
		long deTag = (Long) heads.get(AmqpHeaders.DELIVERY_TAG);
	
			// 正常业务逻辑，当出现异常的时候，走死信队列重新消费。

			System.out.println(deTag);
			// 告诉服务器，已经消费成功,
			channel.basicAck(deTag, false);		 
	}
	
	@RabbitListener(containerFactory="tt", errorHandler = "RabbitConsumerListenerErrorHandler", queues = "test_queue_1")
	@RabbitHandler
	public void delayMessage(Message message, Channel channel, @Headers Map<String, Object> heads) throws Exception {

		System.out.println("分发队列message"+new String(message.getBody()));
		long deTag = (Long) heads.get(AmqpHeaders.DELIVERY_TAG);
	
			// 正常业务逻辑，当出现异常的时候，走死信队列重新消费。

			System.out.println(deTag);
			// 告诉服务器，已经消费成功,
			channel.basicAck(deTag, false);		 
	}
	@RabbitListener(containerFactory="tt", errorHandler = "RabbitConsumerListenerErrorHandler", queues = "TestFanoutQueue")
	@RabbitHandler
	public void faountMessage(Message message, Channel channel, @Headers Map<String, Object> heads) throws Exception {

		System.out.println("延时队列message"+new String(message.getBody()));
		long deTag = (Long) heads.get(AmqpHeaders.DELIVERY_TAG);
	
			// 正常业务逻辑，当出现异常的时候，走死信队列重新消费。

			System.out.println(deTag);
			// 告诉服务器，已经消费成功,
			channel.basicAck(deTag, false);		 
	}
	@RabbitListener(containerFactory="tt", errorHandler = "RabbitConsumerListenerErrorHandler", queues = "TestFanoutQueue2")
	@RabbitHandler
	public void faount2Message(Message message, Channel channel, @Headers Map<String, Object> heads) throws Exception {

		System.out.println("分发队列message"+new String(message.getBody()));
		long deTag = (Long) heads.get(AmqpHeaders.DELIVERY_TAG);
	
			// 正常业务逻辑，当出现异常的时候，走死信队列重新消费。

			System.out.println(deTag);
			// 告诉服务器，已经消费成功,
			channel.basicAck(deTag, false);		 
	}
}
