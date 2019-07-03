package com.te.rpc.processtime;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.te.rpc.serializable.Hessian2Serialization;
import com.te.rpc.serializable.Serialization;

public class RpcSocketConsumer {

    public static void main(String[] args) throws Exception {

        //序列化层实现参考之前的章节
        Serialization serialization = new Hessian2Serialization();

        //开启一个连接端口和提供端进行通信
        Socket socket = new Socket("localhost", 8088);
        // 得到对应的流信息
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        //封装rpc请求
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(12345L);
        //序列化 rpcRequest => requestBody
        //因为RPC调用通过网络进行传输，只支持识别 二进制数据
        byte[] requestBody = serialization.serialize(rpcRequest);
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(requestBody.length);
        dos.write(requestBody);
        dos.flush();
        DataInputStream dis = new DataInputStream(is);
        //进入阻塞等待，提供者的数据返回
        int length = dis.readInt();
        byte[] responseBody = new byte[length];
        dis.read(responseBody);
        //反序列化 responseBody => rpcResponse
        RpcResponse rpcResponse = serialization.deserialize(responseBody, RpcResponse.class);
        is.close();
        os.close();
        socket.close();

        System.out.println(rpcResponse);
    }
}