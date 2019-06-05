package com.te.topic;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class Snippet {
	@Autowired // adminClien需要自己生成配置bean
	private AdminClient adminClient;
	
	
    @Resource
    private KafkaTemplate defaultKafkaTemplate;

	@Test//自定义手动创建topic和分区
	public void testCreateTopic() throws InterruptedException {
		// 这种是手动创建 //10个分区，一个副本
		// 分区多的好处是能快速的处理并发量，但是也要根据机器的配置
		NewTopic topic = new NewTopic("topic.quick.initial2", 10, (short) 1);
		adminClient.createTopics(Arrays.asList(topic));
		Thread.sleep(1000);
	}
	
	@Test//和rabbitMQ的类似
	public void testDefaultKafka(){
		//前提是要在创建模板类的时候指定topic，否则回报找不到topic
		defaultKafkaTemplate.setDefaultTopic("这里发送的消息");
		
		
	}

	@Test // 遍历某个topic信息
	/**
	 * 可得到如下信息 大致可分为 ，分区值(int)，主分区所在位置，从分区（ 副本）位置 isr选举权所在位置 k:
	 * topic.quick.initial ,v: (name=topic.quick.initial, internal=false,
	 * partitions=(partition=0, leader=admin-PC:9092 (id: 0 rack: null),
	 * replicas=admin-PC:9092 (id: 0 rack: null), isr=admin-PC:9092 (id: 0 rack:
	 * null)), (partition=1, leader=admin-PC:9092 (id: 0 rack: null),
	 * replicas=admin-PC:9092 (id: 0 rack: null), isr=admin-PC:9092 (id: 0 rack:
	 * null)), (partition=2, leader=admin-PC:9092 (id: 0 rack: null),
	 * replicas=admin-PC:9092 (id: 0 rack: null), isr=admin-PC:9092 (id: 0 rack:
	 * null)), (partition=3, leader=admin-PC:9092 (id: 0 rack: null),
	 * replicas=admin-PC:9092 (id: 0 rack: null), isr=admin-PC:9092 (id: 0 rack:
	 * null)), (partition=4, leader=admin-PC:9092 (id: 0 rack: null),
	 * replicas=admin-PC:9092 (id: 0 rack: null), isr=admin-PC:9092 (id: 0 rack:
	 * null)), (partition=5, leader=admin-PC:9092 (id: 0 rack: null),
	 * replicas=admin-PC:9092 (id: 0 rack: null), isr=admin-PC:9092 (id: 0 rack:
	 * null)), (partition=6, leader=admin-PC:9092 (id: 0 rack: null),
	 * replicas=admin-PC:9092 (id: 0 rack: null), isr=admin-PC:9092 (id: 0 rack:
	 * null)), (partition=7, leader=admin-PC:9092 (id: 0 rack: null),
	 * replicas=admin-PC:9092 (id: 0 rack: null), isr=admin-PC:9092 (id: 0 rack:
	 * null)), (partition=8, leader=admin-PC:9092 (id: 0 rack: null),
	 * replicas=admin-PC:9092 (id: 0 rack: null), isr=admin-PC:9092 (id: 0 rack:
	 * null)), (partition=9, leader=admin-PC:9092 (id: 0 rack: null),
	 * replicas=admin-PC:9092 (id: 0 rack: null), isr=admin-PC:9092 (id: 0 rack:
	 * null)), (partition=10, leader=admin-PC:9092 (id: 0 rack: null),
	 * replicas=admin-PC:9092 (id: 0 rack: null), isr=admin-PC:9092 (id: 0 rack:
	 * null)))
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	//通过表达式的形式获取topic.quick.initial的数据并进行消费
	public void testSelectTopicInfo() throws ExecutionException, InterruptedException {
		DescribeTopicsResult result = adminClient.describeTopics(Arrays.asList("topic.quick.initial"));
		result.all().get().forEach((k, v) -> System.out.println("k: " + k + " ,v: " + v.toString() + "\n"));
	}
}
