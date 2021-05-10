package com.thunder.test;

import com.thunder.RpcServer;
import com.thunder.api.HelloService;
import com.thunder.registry.DefaultServiceRegistry;
import com.thunder.registry.ServiceRegistry;
import com.thunder.serializer.HessianSerializer;
import com.thunder.serializer.KryoSerializer;
import com.thunder.socket.server.SocketServer;

public class TestServer {
    public static void main (String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.setSerializer(new KryoSerializer());
        socketServer.start(9000);
    }
}
