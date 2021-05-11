package com.thunder.test;

import com.thunder.api.HelloService;
import com.thunder.registry.ServiceRegistry;
import com.thunder.serializer.HessianSerializer;
import com.thunder.serializer.KryoSerializer;
import com.thunder.transport.socket.server.SocketServer;

public class TestServer {
    public static void main (String[] args) {
        HelloService helloService = new HelloServiceImpl();
        SocketServer socketServer = new SocketServer("127.0.0.1",9000);
        socketServer.setSerializer(new HessianSerializer());
        socketServer.publishService(helloService,HelloService.class);
    }
}
