package com.thunder.transport;

import com.thunder.entity.RpcRequest;
import com.thunder.entity.RpcResponse;
import com.thunder.transport.netty.client.NettyClient;
import com.thunder.transport.socket.client.SocketClient;
import com.thunder.util.RpcMessageChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/**
 * RPC客户端动态代理
 */
public class RpcClientProxy implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);

    private final RpcClient client;
    public RpcClientProxy(RpcClient client) {
        this.client = client;
    }
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T>clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
    }


    @Override
    public Object invoke (Object proxy, Method method, Object[] args){
        logger.info("method:{}#{}", method.getDeclaringClass().getName(),method.getName());
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(),method.getDeclaringClass().getName(),method.getName(),args,method.getParameterTypes(),false);

        RpcResponse rpcResponse = null;
        if(client instanceof NettyClient){
            try {
                //异步获取调用结果
                CompletableFuture<RpcResponse> completableFuture = (CompletableFuture<RpcResponse>)client.sendRequest(rpcRequest);
                rpcResponse = completableFuture.get();
            }catch (InterruptedException | ExecutionException e){
                logger.error("方法调用请求发送失败", e);
                return null;
            }
        }
        if(client instanceof SocketClient){
            rpcResponse = (RpcResponse) client.sendRequest(rpcRequest);
        }
        RpcMessageChecker.check(rpcRequest, rpcResponse);
        return rpcResponse.getData();
    }
}
