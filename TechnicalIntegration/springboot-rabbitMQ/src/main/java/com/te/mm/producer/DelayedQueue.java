package com.te.mm.producer;

/**
 * 延时队列，通过插件的方式，不在通过死信交换机
 * @author Administrator
 *
 */
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class DelayedQueue {

	@Bean//自定义交换机，和fanout，topic，direct没什么区别
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("test_exchange", "x-delayed-message",true, false,args);
    }

    @Bean
    public Queue queue() {
        Queue queue = new Queue("test_queue_1", true);
        return queue;
    }

    @Bean
    public Binding binding() {

        return BindingBuilder.bind(queue()).to(delayExchange()).with("test_queue_1").noargs();
    }

}
