package com.te.handler;


import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Service;

/**
 * kafka的回调类，可以在此类中定义producer发送消息失败时候的回调方法
 * @author 刘新杨
 *   菩提本无树，
 *   明镜亦非台。
 */
@Service
public class KafkaSendResultHandler implements ProducerListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaSendResultHandler.class);

	@Override
	public void onSuccess(String topic, Integer partition, Object key, Object value, RecordMetadata recordMetadata) {
		// TODO Auto-generated method stub
		log.info("消息发送成功");
	}

	@Override
	public void onError(String topic, Integer partition, Object key, Object value, Exception exception) {
		// TODO Auto-generated method stub
		//可重试
		System.out.println("消息发送失败");
		
	}

	@Override
	public boolean isInterestedInSuccess() {
		// TODO Auto-generated method stub
		return false;
	}

}
