package com.te.mm.Entity;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * 对象转换器
 * @author 刘新杨
 *   菩提本无树，
 *   明镜亦非台。
 */
public class ObjConsert  implements MessageConverter{

	@Override//将对象转为消息
	public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override//将消息转为java对象
	public Object fromMessage(Message message) throws MessageConversionException {
	
		
		return null;
	}

}
