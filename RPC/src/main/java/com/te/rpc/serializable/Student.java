package com.te.rpc.serializable;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;
/**
 * 聊一聊 Serializable 关键字
 * 带着问题走起来！
 * 1.什么是java序列化机制
 * 2.为什么加了Serializable 关键字就可以实现序列化了
 * 3.为什么变量加了此关键字transient 就无法序列化了
 * 4.为什么加了static关键字也不参与序列化了呢
 * 
 * @author liuxy
  *      一一解答
 * java序列化机制就是当我们要对数据进行传输的时候需要将对象转换成 字符串(String 默认实现了serializable关键字) ，
 * 字节码等
  * 因为只有这样的字节数组，才能在网络上进行传输。反序列化就是将文件或者 byte信息读到程序中
   2.在java Output inPut等关键字.在进行序列化的时候会进行判断类型是 不是 stirng，byte，Serializable 等类型的
   数据对象，是才让他进行传输，否则就报错
   个人认为Serializable 就是一个标记，加了这个关键字说明，我是可以被序列化的对象了。
   3.transient 关键字在进行序列化的时候，可以序列化成功，但是反序列化的时候就无法获取到数据了
   4.static不参与序列化，直接屏蔽的序列化的过程。
 */
@Data
class Student implements Serializable{  
	   /**
	 * 
	 */
	//为什么要加一个序列化版本ID呢。
	/**
	 * 因为序列化有的时候是要通过网络传输的，有一定的被篡改风险。加了版本id后，每次反序列化之后
	 * 可以比对一下版本，看下是否被修改了
	 */
	private static final long serialVersionUID =9856L;
	private String name;  
	
	public Student (String name) {
		this.name =name;
	}
	public Student () {}
	
	} 
