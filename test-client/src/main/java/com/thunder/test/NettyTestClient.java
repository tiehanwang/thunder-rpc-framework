package com.thunder.test;

import com.thunder.serializer.CommonSerializer;
import com.thunder.transport.RpcClient;
import com.thunder.transport.RpcClientProxy;
import com.thunder.api.HelloObject;
import com.thunder.api.HelloService;
import com.thunder.transport.netty.client.NettyClient;
import com.thunder.serializer.ProtostuffSerializer;

public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.PROTOBUF_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "this is netty style");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}