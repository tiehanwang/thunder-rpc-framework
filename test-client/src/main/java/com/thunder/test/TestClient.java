package com.thunder.test;

import com.thunder.api.HelloObject;
import com.thunder.api.HelloService;
import com.thunder.loadbalancer.RoundRobinLoadBalancer;
import com.thunder.serializer.CommonSerializer;
import com.thunder.serializer.HessianSerializer;
import com.thunder.transport.RpcClientProxy;
import com.thunder.serializer.KryoSerializer;
import com.thunder.transport.socket.client.SocketClient;

public class TestClient {
    public static void main (String[] args) {
        SocketClient client = new SocketClient(CommonSerializer.KRYO_SERIALIZER, new RoundRobinLoadBalancer());
        //接口与代理对象之间的中介对象
        RpcClientProxy proxy = new RpcClientProxy(client);
        //创建代理对象
        HelloService helloService = proxy.getProxy(HelloService.class);
        //接口方法的参数对象
        HelloObject object = new HelloObject(12, "This is test message");
        for(int i = 0; i < 20; i++){
            //由动态代理可知，代理对象调用hello()实际会执行invoke()
            String res = helloService.hello(object);
            System.out.println(res);
        }
    }
}
