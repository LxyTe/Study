package com.te.rpc.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibProxy {
	 static BookApi createCglibDynamicProxy(final BookApi delegate) throws Exception {
		 /**
		  * 创建代理类对象
		  */
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new CglibInterceptor(delegate ));
        enhancer.setInterfaces(new Class[]{BookApi.class});
        BookApi cglibProxy = (BookApi) enhancer.create();
        return cglibProxy;
    }
	
	   private static class CglibInterceptor implements MethodInterceptor {

	        final Object delegate;

	        CglibInterceptor(Object delegate) {
	            this.delegate = delegate;
	        }

	        //此方法是一个默认的重写方法，代理类创建的对象，调用其他的方法的时候会直接进入这里，
	        public Object intercept(Object object, Method method, Object[] objects,
	                                MethodProxy methodProxy) throws Throwable {
	            //添加代理逻辑
	            if(method.getName().equals("Te")) {
	                System.out.println("cglib");
	                //通过发射来进行调用
	                return method.invoke(delegate, objects);
	            }
	            return null;
//	            return methodProxy.invoke(delegate, objects);
	        }
	    }
}
