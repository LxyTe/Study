package com.te.rpc.processtime;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;
/**
 * @Desc RPC 请求参数类
 * @author liuxy
 *
 */

@Data
public class RpcRequest implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @Desc 接口名字
	 */
	private String interfaceName; 
	
	/**
	 * @Desc 接口中的方法名字
	 */
    private String methodName;
    /**
     * 
     * @Desc参数说明
     */
    private String parametersDesc;
    /**
     * @Desc 具体参数
     */
    private Object[] arguments;
    /**
     * @Desc 参数附件
     */
    private Map<String, String> attachments;
    /**
     * @Desc 重试次数
     */
    private int retries = 0;
    
    /**
     * @Desc 此次请求id
     */
    private long requestId;
    
    /**
     * @Desc RPC 协议版本
     */
    private byte rpcProtocolVersion;
}