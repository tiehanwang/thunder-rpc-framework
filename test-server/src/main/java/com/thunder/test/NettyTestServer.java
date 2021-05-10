package com.thunder.test;

import com.thunder.api.HelloService;
import com.thunder.netty.server.NettyServer;
import com.thunder.registry.DefaultServiceRegistry;
import com.thunder.registry.ServiceRegistry;
import com.thunder.serializer.HessianSerializer;
import com.thunder.serializer.KryoSerializer;

public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.setSerializer(new HessianSerializer());
        server.start(9999);
    }
}