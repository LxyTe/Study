package com.te.rpc.proxy;

public class TestJdkProxy {

	/**
	 * 这俩动态代理最终实现都是基于反射的。当19行or  14行代码运行的时候就会走intercept 或者invoke
	 * 然后以接口调用实现类，传入参数的方式来进行调用
	 * @param args
	 * @throws Exception
	 */
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
