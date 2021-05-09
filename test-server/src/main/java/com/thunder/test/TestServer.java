package com.thunder.test;

import com.thunder.api.HelloService;
import com.thunder.registry.DefaultServiceRegistry;
import com.thunder.registry.ServiceRegistry;
import com.thunder.server.RpcServer;

public class TestServer {
    public static void main (String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9000);
    }
}
