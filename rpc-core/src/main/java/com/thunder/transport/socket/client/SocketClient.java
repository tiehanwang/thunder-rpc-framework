package com.thunder.transport.socket.client;


import com.thunder.registry.NacosServiceDiscovery;
import com.thunder.registry.NacosServiceRegistry;
import com.thunder.registry.ServiceDiscovery;
import com.thunder.registry.ServiceRegistry;
import com.thunder.transport.RpcClient;
import com.thunder.entity.RpcRequest;
import com.thunder.entity.RpcResponse;
import com.thunder.enumeration.RpcError;
import com.thunder.exception.RpcException;
import com.thunder.serializer.CommonSerializer;
import com.thunder.transport.socket.util.ObjectReader;
import com.thunder.transport.socket.util.ObjectWriter;
import com.thunder.util.RpcMessageChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 进行远程调用的客户端
 */
public class SocketClient implements RpcClient  {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private final ServiceDiscovery serviceDiscovery;
    private final CommonSerializer serializer;

    public SocketClient() {
        this(DEFAULT_SERIALIZER);
    }

    public SocketClient(Integer serializerCode) {
        serviceDiscovery = new NacosServiceDiscovery();
        serializer = CommonSerializer.getByCode(serializerCode);
    }
    @Override
    public Object sendRequest (RpcRequest rpcRequest) {
        if(serializer == null){
            logger.error("not set serializer!");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        //从Nacos获取提供对应服务的服务端地址
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, rpcRequest, serializer);
            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
            RpcMessageChecker.check(rpcRequest, rpcResponse);
            return rpcResponse;
        } catch (IOException e) {
            logger.error("error happened" + e);
            throw new RpcException("error", e);
        }
    }

}
