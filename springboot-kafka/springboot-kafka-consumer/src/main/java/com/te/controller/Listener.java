package com.te.controller;

import java.util.List;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;

import lombok.extern.slf4j.Slf4j;

/**
 * 同一个消费者组中的消费者不能指定同一分区
 * @author 刘新杨
 *   菩提本无树，
 *   明镜亦非台。
 */
@Slf4j
public class Listener {

	@Autowired
    private KafkaListenerEndpointRegistry registry;
	   private Logger log = LoggerFactory.getLogger(this.getClass());
	 @KafkaListener(topics = {"test"},id="consumerId",errorHandler="consumerAwareErrorHandler")//id的值可为消费者组id
	    public void listen(ConsumerRecord<?, ?> record) {
	//errorHandler异常处理器注解里面使用的也是bean的名称
//	        Log.info("kafka的key: " + record.key());0
//	        Log.info("kafka的value: " + record.value().toString());
	    }
	 //消费名称为test的topic中的第0个分区
	 @KafkaListener(id="consumerId",	
			 topicPartitions={@TopicPartition(topic="test",partitions={"0"})
		//   @TopicPartition(topic = "topic.quick.batch.partition",partitions = {"0","4"}也可指定多个分区
			})
	    public void listen2(ConsumerRecord<?, ?> record,Acknowledgment ack) {
		 
//	        Log.info("kafka的key: " + record.key());
//	        Log.info("kafka的value: " + record.value().toString());
		 ack.acknowledge();
	    }
	 /**
	  * 下面消费者监听器的详细解释
	  * 可批量消费的，使用的时候要注意containerFactory
	  */
	  @KafkaListener(id = "consumerId",topics = {"test.batch"},containerFactory = "kafkaListenerContainerFactory")
	    public void batchListener(List<String> data,Acknowledgment ack,Consumer consumer) {
		
	        log.info("topic.quick.batch  receive : ");
	        for (String s : data) {
	            log.info(  s);
	        }
	        
	        ack.acknowledge();//调用了则表示确认消费，某个偏移量的消息
	        /**
	         * 使用Consumer.seek方法，重新回到该未ack消息偏移量的位置重新消费，
	         * 这种可能会导致死循环，原因出现于业务一直没办法处理这条数据，
	         * 但还是不停的重新定位到该数据的偏移量上。
	         */
	      //  consumer.seek(arg0, arg1);
	    }
	  /**
	   * 获取消息的生产者中配置消息头参数
	   */
	  @KafkaListener(id = "anno", topics = "topic.quick.anno")
	    public void annoListener(@Payload String data,
	                             @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) Integer key,
	                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
	                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
	                             @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {
	        log.info("topic.quick.anno receive : \n"+
	            "data : "+data+"\n"+
	            "key : "+key+"\n"+
	            "partitionId : "+partition+"\n"+
	            "topic : "+topic+"\n"+
	            "timestamp : "+ts+"\n"
	        );

	    }
	  
	  /**
	   * 下面监听器的意思为topic.quick.target中的消息，消息完成之后，会把return里面的内容发送给topic.quick.real(topic)
	   * 可以被其它消费者进行消费。可用于A执行完流程，调用B
	   * @param data
	   * @return
	   */
	  @KafkaListener(id = "forward", topics = "topic.quick.target")
	    @SendTo("topic.quick.real")
	    public String forward(String data) {
	        log.info("topic.quick.target  forward "+data+" to  topic.quick.real");
	        return "topic.quick.target send msg : " + data;
	    }
	  
	
//	    //定时器，每天凌晨0点开启监听
//	    @Scheduled(cron = "0 0 0 * * ?")
//	    public void startListener() {
//	        log.info("开启监听");
//	        //判断监听容器是否启动，未启动则将其启动
//	        if (!registry.getListenerContainer("durable").isRunning()) {
//	            registry.getListenerContainer("durable").start();
//	        }
//	        registry.getListenerContainer("durable").resume();
//	    }
//
//	    //定时器，每天早上10点关闭监听
//	    @Scheduled(cron = "0 0 10 * * ?")
//	    public void shutDownListener() {
//	        log.info("关闭监听");
//	        registry.getListenerContainer("durable").pause();
//	    }

}
