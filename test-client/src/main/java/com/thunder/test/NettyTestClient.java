package com.thunder.test;

import com.thunder.RpcClient;
import com.thunder.RpcClientProxy;
import com.thunder.api.HelloObject;
import com.thunder.api.HelloService;
import com.thunder.netty.client.NettyClient;
import com.thunder.serializer.HessianSerializer;

public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 9999);
        client.setSerializer(new HessianSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "this is netty style");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}