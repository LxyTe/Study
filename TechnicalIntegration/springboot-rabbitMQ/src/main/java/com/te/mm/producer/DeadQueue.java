package com.te.mm.producer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 此类为死信队列配置类 
 * 可以达到演示消费的效果
 * @author Administrator
 *
 */
@Configuration
public class DeadQueue {
	
	
	public static final String Test_Topic_Queue = "TestQueue";
	public static final String Test_Topic_Change = "Test_Change";
	public static final String Test_Topic_ROUTING_KEY = "MM";
	/**
	 * 定义死信队列相关信息
	 */
	public final static String deadQueueName = "dead_queue";
	public final static String deadRoutingKey = "dead_routing_key";
	public final static String deadExchangeName = "dead_exchange";
	/**
	 * 死信队列 交换机标识符 这些value都是固定格式的,否则无法识别
	 */
	public static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";
	/**
	 * 死信队列交换机绑定键标识符
	 */
	public static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

	/**
	 * 配置死信队列
	 * 将死信队列绑定在topic的测试队列中
	 * 意思为当TestQueue被拒绝消费，或者设置了ttl并且过期的情况下就可以
	 * 使用死信队列了
	 * @return
	 */
	
	@Bean
	public Queue queue() {
		Map<String, Object> args = new HashMap<>(2);
		//设置一些参数，
		args.put(DEAD_LETTER_QUEUE_KEY, deadExchangeName);
		args.put(DEAD_LETTER_ROUTING_KEY, deadRoutingKey);
		
		Queue queue = new Queue(Test_Topic_Queue, true, false, false, args);
		return queue;
	}
	

	@Bean//死信队列绑定死信交换机
	 public Binding bindingTopicExchangeMessage() {
		return BindingBuilder.bind(queue()).to(deadExchange()).with(Test_Topic_ROUTING_KEY);
	}

	@Bean//死信转发队列(相当于一个普通队列)
	public Queue deadTestQueue() {
		Queue queue = new Queue(deadQueueName, true);
		return queue;
	}
	@Bean
	public DirectExchange deadExchange() {
		return new DirectExchange(deadExchangeName);
	}

	@Bean//普通队列绑定死信交换机 
	public Binding bindingDeadExchange() {
		return BindingBuilder.bind(deadTestQueue()).to(deadExchange()).with(deadRoutingKey);
	}

}
