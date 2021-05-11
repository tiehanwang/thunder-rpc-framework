package com.thunder.test;

import com.thunder.api.HelloService;
import com.thunder.registry.ServiceRegistry;
import com.thunder.serializer.CommonSerializer;
import com.thunder.serializer.HessianSerializer;
import com.thunder.serializer.KryoSerializer;
import com.thunder.transport.RpcServer;
import com.thunder.transport.socket.server.SocketServer;

public class TestServer {
    public static void main (String[] args) {
        RpcServer server = new SocketServer("127.0.0.1", 9998, CommonSerializer.KRYO_SERIALIZER);
        server.start();
    }
}
