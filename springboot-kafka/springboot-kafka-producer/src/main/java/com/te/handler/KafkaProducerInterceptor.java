package com.te.handler;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KafkaProducerInterceptor  implements ProducerInterceptor<String, String>{

	@Override
	public void configure(Map<String, ?> configs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
		// TODO Auto-generated method stub
		/**
		 * 可以在此方法中定义对消息做一些特殊处理
		 */
		return record;
	}

	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
		// TODO Auto-generated method stub
		/**
		 * 该方法会在消息被应答之前或消息发送失败的时候调用，也可以在此做重试操作
		 * 
		 */
		metadata.partition();
		metadata.topic();
		metadata.serializedKeySize();
		metadata.serializedValueSize();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
