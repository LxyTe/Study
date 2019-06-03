//package com.te.mm.factoryconfig;
//
//import org.springframework.amqp.core.AcknowledgeMode;
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.te.mm.Entity.ObjConsert;
//
//
//
///**
// * 消费者仓库配置，模仿kafka
// * @author 刘新杨
// *   菩提本无树，
// *   明镜亦非台。
// */
//
//@Configuration
//public class ConsumerFactoryConfig {
//
//
//	@Bean(name="tt")//和kafka的配置仓库类一莫一样很牛逼的
//	public RabbitListenerContainerFactory<SimpleMessageListenerContainer> tt(){
//		//ConcurrentKafkaListenerContainerFactory
//
//		//消息的统一过滤器
//		MessageConverter messageConverter = new ObjConsert();
//		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//		factory.setConcurrentConsumers(5);//允许同时消费数量为5
//		factory.setMaxConcurrentConsumers(10);//允许同时最大消费数量为10
//		factory.setReceiveTimeout(10000L);//10秒
//	    factory.setMessageConverter(messageConverter);//具体的逻辑要自己在ObjConsert里面写
//		factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);//设置手动提交
//		factory.setConnectionFactory(connectionFactory);
//	    return  factory;
//	}
//
//
//	
//}
