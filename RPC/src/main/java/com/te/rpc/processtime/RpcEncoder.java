package com.te.rpc.processtime;

import com.te.rpc.serializable.Hessian2Serialization;
import com.te.rpc.serializable.Serialization;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    Serialization serialization = new Hessian2Serialization();

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = serialization.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}