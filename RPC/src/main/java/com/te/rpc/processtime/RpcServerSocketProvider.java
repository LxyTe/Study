package com.te.rpc.processtime;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.te.rpc.serializable.Hessian2Serialization;
import com.te.rpc.serializable.Serialization;

/**
 * @Desc 基于socket进行传输测试
 * @author liuxy
 * Disadvantage
 *           服务的提供者的每次监听都调用了一个线程，在并发量小的时候问题不大，
 *           但是当并发量特别高的时候，你线程肯定不够用，系统就凉了。
 *           所以dubbo和springcloud等 RPC框架，都使用了Netty来进行实现传输
 */
public class RpcServerSocketProvider {


    public static void main(String[] args) throws Exception {

        //序列化层实现参考之前的章节
        Serialization serialization = new Hessian2Serialization();

        //开启一个传输端口
        ServerSocket serverSocket = new ServerSocket(8088);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        while (true) {
        	//进入阻塞，等待消费者的连接，然后触发34行代码
            final Socket socket = serverSocket.accept();
            executorService.execute(() -> {
                try {
                	//服务消费者走到36行，进行阻塞。然后提供者根据 消费者35之前的  dos.flush()操作;会触发解除阻塞
                    InputStream is = socket.getInputStream();
                    OutputStream os = socket.getOutputStream();
                    try {
                        DataInputStream dis = new DataInputStream(is);
                        //阻塞式的获取
                        int length = dis.readInt();
                        byte[] requestBody = new byte[length];
                        //也是阻塞式的获取
                        dis.read(requestBody);
                        //反序列化requestBody => RpcRequest
                        RpcRequest rpcRequest = serialization.deserialize(requestBody, RpcRequest.class);
                        //反射调用生成响应 并组装成 rpcResponse
                        RpcResponse rpcResponse = invoke(rpcRequest);
                        //序列化rpcResponse => responseBody
                        byte[] responseBody = serialization.serialize(rpcResponse);
                        DataOutputStream dos = new DataOutputStream(os);
                        dos.writeInt(responseBody.length);
                        dos.write(responseBody);
                        dos.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        is.close();
                        os.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public static RpcResponse invoke(RpcRequest rpcRequest) {
    	/**
    	 * 正常情况下使用的是通过 接口名字，方法名字，和参数进行反射调用，然后返回其结果
    	 */
        //模拟反射调用
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        rpcResponse.setValue("反射调用结果后返回");
        //... some operation
        return rpcResponse;
    }

}