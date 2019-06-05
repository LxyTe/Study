package com.te.handler;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.stereotype.Component;

@Component
public class KafkaRecordFilterStrategy implements RecordFilterStrategy {

	@Override
	public boolean filter(ConsumerRecord consumerRecord) {
	//此类可以对即将消费的信息进行一些列的过滤
    //比如写日志的时候，过滤掉一些日志不消费，也是可以的，但是不消费，那条消息就会被丢弃
	//为true则丢弃消息
		return false;
	}

}
