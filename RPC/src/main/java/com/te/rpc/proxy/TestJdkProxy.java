package com.te.rpc.proxy;

public class TestJdkProxy {

	
	public static void main(String[] args) throws Exception {
		//new一个实现类对象，以供调用
		BookApi bookApi =new BookImpl();
//		BookApi JdkDynamicProxy = JdkProxy.createJdkDynamicProxy(bookApi );
//		System.out.println(JdkDynamicProxy.Te("JDK代理测试"));
		/**
		 * cglib调用方式,几乎一摸一样，就是用了自己的api封装
		 */
		BookApi CglibDynamicProxy = CglibProxy.createCglibDynamicProxy(bookApi);
		System.out.println(CglibDynamicProxy.Te("Cglib代理测试"));
	}
}
