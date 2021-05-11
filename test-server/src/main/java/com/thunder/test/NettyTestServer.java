package com.thunder.test;

import com.thunder.api.HelloService;
import com.thunder.transport.netty.server.NettyServer;
import com.thunder.registry.ServiceRegistry;
import com.thunder.serializer.ProtostuffSerializer;

public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer server = new NettyServer("127.0.0.1",9999);
        server.setSerializer(new ProtostuffSerializer());
        server.publishService(helloService,HelloService.class);
    }
}