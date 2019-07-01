package com.te.rpc;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 此为RPC框架原理机制
 *  何为RPC？
 *  1.我理解的就是不在本地进行调用了，不在直接从controller层直接调用service层
 *  而是通过别人来进行调用，我只拿到结果就可以了,大大减少了代码的之间的冗余
 * 出自阿里徐靖峰
 * 
 * RPC框架现为所有分布式框架基础，必须要进行掌握
 * 建议debug一行一行走
 * @author liuxy
 *
 */

public class RpcFramework {

    /**
     * 暴露服务
     *
     * @param service 服务实现
     * @param port 服务端口
     * @throws Exception
     */
    public static void export(final Object service, int port) throws Exception {
        if (service == null)
            throw new IllegalArgumentException("service instance == null");
        if (port <= 0 || port > 65535)
            throw new IllegalArgumentException("Invalid port " + port);
        System.out.println("Export service " + service.getClass().getName() + " on port " + port);
        ServerSocket server = new ServerSocket(port);
        for(;;) {
            try {
            	//等待客户端连接，这里相当于在等待消费者发送消息，没有消费者就进行阻塞，有消费者了就new一个线程
                final Socket socket = server.accept();
                //生成一个内部线程
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            try {
                            	//得到输入流里面的东西，这里是一个Socket[addr=/127.0.0.1,port=52453,localport=1234]
                                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                                try {
                                	//得到方法名字，这里是一个接口名字。这个接口名字就是consumer里面调用的接口名字
                                    String methodName = input.readUTF();
                                    Class<?>[] parameterTypes = (Class<?>[])input.readObject();
                                    //得到调用hello方法，传入的名字
                                    Object[] arguments = (Object[])input.readObject();
                                    //生成一个socket输出流
                                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                                    try {
                                    	//通过反射技术得到hello方法的实现类
                                        Method method = service.getClass().getMethod(methodName, parameterTypes);
                                        //通过得到的实现类，然后调用service(com.te.rpc.HelloServiceImpl@4e81cf3)也就是具体的实现方法
                                        //arguments(arguments)就是参数 World0
                                        Object result = method.invoke(service, arguments);
                                      //输出流输出结果
                                        output.writeObject(result);
                                    } catch (Throwable t) {
                                    	//
                                        output.writeObject(t);
                                    } finally {
                                        output.close();
                                    }
                                } finally {
                                    input.close();
                                }
                            } finally {
                                socket.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 引用服务
     *
     * @param <T> 接口泛型
     * @param interfaceClass 接口类型
     * @param host 服务器主机名
     * @param port 服务器端口
     * @return 远程服务
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T refer(final Class<T> interfaceClass, final String host, final int port) throws Exception {
        if (interfaceClass == null)
            throw new IllegalArgumentException("Interface class == null");
        if (! interfaceClass.isInterface())
            throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class!");
        if (host == null || host.length() == 0)
            throw new IllegalArgumentException("Host == null!");
        if (port <= 0 || port > 65535)
            throw new IllegalArgumentException("Invalid port " + port);
        //典型的通过接口的class来得到具体的接口信息
        System.out.println("Get remote service " + interfaceClass.getName() + " from server " + host + ":" + port);
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] {interfaceClass}, new InvocationHandler() {
         //通过interfaceClass接口的信息，搞一个代理类，然后把接口名字，和参数都传给export 服务实现类
        	//服务实现类得到接口名字通过反射得到了接口的实现类，然后传入参数，这里在通过 input.readObject();
        	//得到调用方法的返回结果
        	public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
               //建立socket连接,此时就已经和提供者有了连接了 
            	Socket socket = new Socket(host, port);
                try {
                	//先得到一个输出流
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    try {
                    	//下面输出的东西都从49行开始得到
                    	//把接口中的名字输出
                        output.writeUTF(method.getName());
                        //把类对象输出
                        output.writeObject(method.getParameterTypes());
                        //把参数输出
                        output.writeObject(arguments);
                        //得到由64行输出的结果
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        try {
                            Object result = input.readObject();
                            if (result instanceof Throwable) {
                                throw (Throwable) result;
                            }
                            return result;
                        } finally {
                            input.close();
                        }
                    } finally {
                        output.close();
                    }
                } finally {
                    socket.close();
                }
            }
        });
    }
}
