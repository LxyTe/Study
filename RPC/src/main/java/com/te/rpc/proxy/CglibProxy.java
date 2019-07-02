package com.te.rpc.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibProxy {
	 static BookApi createCglibDynamicProxy(final BookApi delegate) throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new CglibInterceptor(delegate));
        enhancer.setInterfaces(new Class[]{BookApi.class});
        BookApi cglibProxy = (BookApi) enhancer.create();
        return cglibProxy;
    }
	
	   private static class CglibInterceptor implements MethodInterceptor {

	        final Object delegate;

	        CglibInterceptor(Object delegate) {
	            this.delegate = delegate;
	        }

	        public Object intercept(Object object, Method method, Object[] objects,
	                                MethodProxy methodProxy) throws Throwable {
	            //Ìí¼Ó´úÀíÂß¼­
	            if(method.getName().equals("Te")) {
	                System.out.println("cglib");
	                return method.invoke(delegate, objects);
	            }
	            return null;
//	            return methodProxy.invoke(delegate, objects);
	        }
	    }
}
