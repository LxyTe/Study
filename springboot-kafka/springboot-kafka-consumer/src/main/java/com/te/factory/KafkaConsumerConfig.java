package com.te.factory;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.TopicPartitionInitialOffset;
import org.springframework.messaging.Message;

import com.te.controller.Listener;
import com.te.handler.KafkaRecordFilterStrategy;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${kafka.consumer.servers}")
    private String servers;
    @Value("${kafka.consumer.enable.auto.commit}")//是否自动提交,建议为false
    private boolean enableAutoCommit;
    @Value("${kafka.consumer.session.timeout}")
    private String sessionTimeout;
    @Value("${kafka.consumer.auto.commit.interval}")
    private String autoCommitInterval;
    @Value("${kafka.consumer.group.id}")
    private String groupId;
    @Value("${kafka.consumer.auto.offset.reset}")
    private String autoOffsetReset;
    @Value("${kafka.consumer.concurrency}")
    private int concurrency;
  
  
    @Autowired
    private KafkaRecordFilterStrategy kafkaRecordFilterStrategy;
    
    /**
     * 下面的工厂类可扩展的东西是真的多，spring也为rabbitMQ准备了一套
     * @return
     */
    //此bean主要用于扩展监听器使用(监听容器工厂)\
    //使用的时候可以直接在@KafkaListener(containerFactory	="bean的名字")即可和rabbiMQ一样
    @Bean(name="kafkaListenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
     
        factory.setConsumerFactory(consumerFactory());
   
        //设置拉取等待时间(也可间接的理解为延时消费)
        factory.getContainerProperties().setPollTimeout(1500);
        //设置并发量，小于或等于Topic的分区数,并且要在consumerFactory设置一次拉取的数量
        factory.setConcurrency(concurrency);
        //设置为批量监听
        factory.setBatchListener(true);      
        //指定使用此bean工厂的监听方法，消费确认为方式为用户指定aks,可以用下面的配置也可以直接使用enableAutoCommit参数
        factory.getContainerProperties().setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);
       
        //设置回复模板，类似于rabbitMQ的死信交换机，但是还有区别,
     //   factory.setReplyTemplate(kafkaTemplate());//发送消息的模板，这里只是消费者的类，所以木有
    
        //禁止自动启动,用于持久化操作，可先将消息都发送至broker，然后在固定的时间内进行持久化，有丢失消息的风险
        factory.setAutoStartup(false);
        
        //使用过滤器
      //配合RecordFilterStrategy使用，被过滤的信息将被丢弃
        factory.setAckDiscarded(true);
        factory.setRecordFilterStrategy(kafkaRecordFilterStrategy);

        return factory;
    }

    @Bean //消费者工厂
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }


    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        //一次拉取消息数量
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "50");
        return propsMap;
    }

    @Bean
    public Listener listener() {
        return  new com.te.controller.Listener();
    }
    
    /**
     * 自定义消费者，不用在去写方法，直接以bean的模式就行创建
     * @return
     */
    @Bean//此bean的作用相当于 @KafkaListener(id="consumerId",			
	// topicPartitions={@TopicPartition(topic="test",partitions={"1"})}),不用再另起监听注解了
    public KafkaMessageListenerContainer demoListenerContainer() {
    	  TopicPartitionInitialOffset topicPartitionInitialOffset =new TopicPartitionInitialOffset("topic.quick.bean",1);
       
    	  ContainerProperties properties = new ContainerProperties(topicPartitionInitialOffset);
        
        properties.setGroupId("bean");
      
  
        properties.setMessageListener(new MessageListener<Integer,String>() {
            private Logger log = LoggerFactory.getLogger(this.getClass());
            @Override
            public void onMessage(ConsumerRecord<Integer, String> record) {
                log.info("topic.quick.bean receive : " + record.toString());
            }
        });

        return new KafkaMessageListenerContainer(consumerFactory(), properties);
    }
    
 

}