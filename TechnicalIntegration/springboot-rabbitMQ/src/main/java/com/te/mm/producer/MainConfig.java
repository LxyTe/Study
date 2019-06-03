package com.te.mm.producer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @using exchange OR queue OR 绑定 的创建类
 * @author
 *
 */
@Configuration

public class MainConfig {


	public static final String Test_Direct_Queue = "TestDirectQueue";
	public static final String Test_Fanout_Queue = "TestFanoutQueue";
	public static final String Test_Fanout_Queue2 = "TestFanoutQueue2";
	//快速发送的队列
	public static final String FastsendingQueue = "fastSending";
	
	

	public static final String Test_Direct_Change = "Test_DirectChange";
	public static final String Test_Fanout_Change = "Test_FanoutChange";

	public static final String FastsendingChange = "fastSendingChange";
	

	public static final String Test_Direct_ROUTING_KEY = "Te";
	
	public static final String FastsendingRouting_Key = "ks";

	@Bean
	public Queue directQueue() {
		return new Queue(Test_Direct_Queue);
	}

	@Bean
	public Queue fastSendingQueue() {
		return new Queue(FastsendingQueue);
	}
	
	@Bean
	public Queue fanoutQueue() {
		return new Queue(Test_Fanout_Queue);
	}
	@Bean
	public Queue fanout2Queue() {
		return new Queue(Test_Fanout_Queue2);
	}


	@Bean // 此处声明的是一个direct交换机，可以根据一个字符来进行匹配
	public DirectExchange directExchange() {
		return new DirectExchange(Test_Direct_Change);
	}
	
	@Bean // 此处声明的是一个快速发送交换机，
	public DirectExchange fastSendingExchange() {
		return new DirectExchange(FastsendingChange);
	}

	@Bean // 此处声明的是一个fanout交换机，可以将消息路由至所有绑定至此交换机的队列
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange(Test_Fanout_Change);
	}


	// @Bean
	// public Binding binding(){
	// //此处应该直接new一个BindingBuilder否则，在监听的时候还要指定交换机
	// return new Binding("spring.boot-one", Binding.DestinationType.QUEUE,
	// "springboot-exchange", "lj", null);
	// }

	@Bean
	 public Binding bindingDirectExchangeMessage() {
		return BindingBuilder.bind(directQueue()).to(directExchange()).with(Test_Direct_ROUTING_KEY);
	}
	@Bean//fanout不需要设置绑定key，设置了也没意义因为它是路由的（一个交换机绑定俩队列）
	 public Binding bindingFanoutExchangeMessage() {
		return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange());
	}
	 public Binding bindingFanout2ExchangeMessage() {
			return BindingBuilder.bind(fanout2Queue()).to(fanoutExchange());
		}
	@Bean
	 public Binding bindingfastSendingExchangeMessage() {
		return BindingBuilder.bind(fastSendingQueue()).to(fastSendingExchange()).with(FastsendingRouting_Key);
	}
}
