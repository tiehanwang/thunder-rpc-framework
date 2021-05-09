package com.thunder.test;

import com.thunder.api.HelloService;
import com.thunder.netty.server.NettyServer;
import com.thunder.registry.DefaultServiceRegistry;
import com.thunder.registry.ServiceRegistry;

public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9999);
    }
}