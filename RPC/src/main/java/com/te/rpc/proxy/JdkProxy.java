package com.te.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * jdk代理类的实现
 * @author liuxy
 *
 */
public class JdkProxy {
	//创建一个jdk动态代理
	/**
	 *  生成一个动态代理类， 不用自己来调用实现，造一个第三方，来隐藏具体的实现
	 * @param delegate
	 * @return
	 */
		static BookApi createJdkDynamicProxy(final BookApi delegate) {
	        BookApi jdkProxy = (BookApi) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
	                new Class[]{BookApi.class}, new JdkHandler(delegate));
	        return jdkProxy;
	}
		/**
		 * 如果调用了具体的方法那么会走下面的invoice方法 传入的参数 delegate 就是你调用的方法，系列中是Te
		 * @author liuxy
		 *
		 */
		
		//jdk动态代理类，是基于接口实现的
		private static class JdkHandler implements InvocationHandler {

	        final Object delegate;

	        JdkHandler(Object delegate) {
	            this.delegate = delegate;
	        }

	        public Object invoke(Object object, Method method, Object[] objects)
	                throws Throwable {
	        	//只有调用了方法只有才会走44行的判断
	            //添加代理逻辑<1>
	            if(method.getName().equals("Te")){
	                System.out.println("代理类的实现");
	                /**
	                                  *      通过反射机制来调用，具体的方法
	                                  *      接口调用实现
	                 */
	               // Method metho = delegate.getClass().getMethod(method.getName(), method.getParameterTypes());
	                //delegate就是实现类的地址com.te.rpc.proxy.BookImpl@3fb6a447
	                //method 是public abstract java.lang.String com.te.rpc.proxy.BookApi.Te(java.lang.String)
	                 
	                return  method.invoke(delegate,objects);
	            }
	            return null;
//	            return method.invoke(delegate, objects);
	        }
		}
}
