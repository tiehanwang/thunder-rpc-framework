package com.thunder.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 传输格式（传输协议）：客户端向服务端传输的对象
 */
@Data
@Builder
public class RpcRequest implements Serializable {
    //函数名
    private String interfaceName;
    //调用参数方法名
    private String methodName;
    //待调用方法的参数
    private Object[] parameters;
    //带调用方法参数的类型
    private Class<?>[] paramTypes;
}
