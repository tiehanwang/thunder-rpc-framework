package com.thunder.test;

import com.thunder.api.HelloObject;
import com.thunder.api.HelloService;
import com.thunder.RpcClientProxy;
import com.thunder.serializer.HessianSerializer;
import com.thunder.serializer.KryoSerializer;
import com.thunder.socket.client.SocketClient;

public class TestClient {
    public static void main (String[] args) {
        SocketClient client = new SocketClient("127.0.0.1",9000);
        client.setSerializer(new KryoSerializer());
        //接口与代理对象之间的中介对象
        RpcClientProxy proxy = new RpcClientProxy(client);
        //创建代理对象
        HelloService helloService = proxy.getProxy(HelloService.class);
        //接口方法的参数对象
        HelloObject object = new HelloObject(12, "This is test message");
        //由动态代理可知，代理对象调用hello()实际会执行invoke()
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
