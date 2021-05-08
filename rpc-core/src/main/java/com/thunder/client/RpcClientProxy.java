package com.thunder.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * RPC客户端动态代理
 */
public class RpcClientProxy implements InvocationHandler {
    private String host;
    private int port;

    @Override
    public Object invoke (Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
