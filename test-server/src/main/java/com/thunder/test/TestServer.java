package com.thunder.test;

import com.thunder.api.HelloService;
import com.thunder.server.RpcServer;

public class TestServer {
    public static void main (String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();

        //注册helloServiceImpl服务
        rpcServer.register(helloService,9000);
    }
}
