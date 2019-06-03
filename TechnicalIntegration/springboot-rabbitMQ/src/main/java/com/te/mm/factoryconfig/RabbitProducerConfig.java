package com.te.mm.factoryconfig;



import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        connectionFactory.setPublisherConfirms(true);//是否开启发布确认
        connectionFactory.setPublisherReturns(true); //失败返回
        return connectionFactory;
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
