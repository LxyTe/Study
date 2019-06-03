package com.te;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.te.mm.Entity.Order;
import com.te.mm.controller.SendController;
import com.te.uuid.UniqueOrderGenerate;


@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan({ "com.te.*" })
public class RabbitMqSpringBootProviderApplicationTests {

	@Test
	public void contextLoads() {
	}
	//雪花算法对象
		private static UniqueOrderGenerate idWorker = new UniqueOrderGenerate(0, 0);
//	@Autowired
//	private RabbitSender rabbitSender;
	@Autowired
	private SendController sendController;
	
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     @Test
     public void testSender() throws Exception{
    	Map<String, Object> properties =new HashMap<>();
        properties.put("number", idWorker.get());
        properties.put("send_time", simpleDateFormat.format(new Date()));

        Order order = new Order();
        order.setName("mm");
        order.setLove("love you2");

        Order order1 = new Order();
        order1.setName("te");
        order1.setLove("is  me2");
        Order order2 = new Order();
        order2.setName("te love mm");
        order2.setLove("GOgo2");
        List<Order>tt=new ArrayList<>();
        tt.add(order);
        tt.add(order1);
        tt.add(order2);
        sendController.sendDirect(tt, properties);
     }
     
     @Test//测试死信队列
     public void testDeadSender() throws Exception{
    	 Map<String, Object> properties =new HashMap<>();
    	 properties.put("number", idWorker.get());
         properties.put("send_time", simpleDateFormat.format(new Date()));
        
         sendController.sendTopic("死信队列的测试", properties);
     }
     @Test//测试延时队列
     public void testDelayedSender() throws Exception{
    	 Map<String, Object> properties =new HashMap<>();
    	 properties.put("number", idWorker.get());
         properties.put("send_time", simpleDateFormat.format(new Date()));
        
         sendController.sendDelayedDirect("延时队列的测试", properties);
     }
     @Test//测试分发队列
     public void testFanoutSender() throws Exception{
    	 Map<String, Object> properties =new HashMap<>();
    	 properties.put("number", idWorker.get());
         properties.put("send_time", simpleDateFormat.format(new Date()));
        
         sendController.sendFanout("分发队列的测试", properties);
     }
}

