package com.te.mm.factoryconfig;



import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.te.mm.Entity.ObjConsert;
/**
 * 创建一个生产者工厂，然后注册到spring容器中，当然我们也可以使用springboot的自动装配
 * 
 * 自动装备所在的包package org.springframework.boot.autoconfigure.amqp;
 * @ConfigurationProperties(prefix = "spring.rabbitmq") RabbitProperties.class
 * 对springboot自动装配有过了解的童鞋，就你那个看懂上面springboot是如何进行对MQ装配的了
 * @author Administrator
 *
 */

@Configuration
@EnableRabbit
public class RabbitProducerConfig {


	 
    @Value("${spring.rabbitmq.host}")
    private String host;
 
    @Value("${spring.rabbitmq.port}")
    private int port;
 
    @Value("${spring.rabbitmq.username}")
    private String username;
 
    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHosthost;

    @Bean
    public ConnectionFactory connectionFactory() {
      
    	CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host,port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHosthost);
        return connectionFactory;
    }
 
	@Bean(name="tt")//和kafka的配置仓库类一莫一样很牛逼的
	public RabbitListenerContainerFactory<SimpleMessageListenerContainer> tt(){
		//ConcurrentKafkaListenerContainerFactory

		//消息的统一过滤器
		MessageConverter messageConverter = new ObjConsert();
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConcurrentConsumers(5);//允许同时消费数量为5
		factory.setMaxConcurrentConsumers(10);//允许同时最大消费数量为10
		factory.setReceiveTimeout(10000L);//10秒
	    factory.setMessageConverter(messageConverter);//具体的逻辑要自己在ObjConsert里面写
		factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);//设置手动提交
		factory.setConnectionFactory(connectionFactory());
	    return  factory;
	}
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
      //template.setDefaultReceiveQueue(queue);//设置默认接收队列
        return template;
    }

    @Bean(name="defauilTemplate")
    public RabbitTemplate rabbitTemplateDefault() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
      template.setDefaultReceiveQueue("fastSending");//设置默认接收队列
        return template;
    }

}
