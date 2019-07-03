package com.te.rpc.processtime;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

/**
 * @Desc 响应
 * @author liuxy
 *
 */
@Data
public class RpcResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @Desc  响应数据
	 */
    private Object value;
    /**
     * @Desc 响应异常
     */
    private Exception exception;
    
    /**
     * @Desc 请求id,和请求类的id相同的
     */
    private long requestId;
    /**
     * @Desc 响应时间
     */
    private long processTime;
    /**
     * @Desc  超时时间
     */
    private int timeout;
    /**
     * @Desc 参数附件
     * // rpc协议版本兼容时可以回传一些额外的信息
     */
    private Map<String, String> attachments;
    /**
     * @Desc RPC协议版本
     */
    private byte rpcProtocolVersion;
}
