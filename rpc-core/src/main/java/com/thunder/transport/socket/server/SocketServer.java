package com.thunder.transport.socket.server;

import com.thunder.handler.RequestHandler;
import com.thunder.enumeration.RpcError;
import com.thunder.exception.RpcException;
import com.thunder.provider.ServiceProvider;
import com.thunder.provider.ServiceProviderImpl;
import com.thunder.registry.NacosServiceRegistry;
import com.thunder.registry.ServiceRegistry;
import com.thunder.serializer.CommonSerializer;
import com.thunder.transport.RpcServer;
import com.thunder.util.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;


/**
 * 进行远程调用连接的服务端
 */
public class SocketServer implements RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);


    private final ExecutorService threadPool;
    private final ServiceRegistry serviceRegistry;
    private final RequestHandler requestHandler = new RequestHandler();

    private final String host;
    private final int port;
    private CommonSerializer serializer;
    private final ServiceProvider serviceProvider;

    public SocketServer(String host, int port){
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
        //创建线程池
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
    }
    /**
     * 服务启动
     */
    public void start(){
        if (serializer == null){
            logger.error("not set serializer!");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("Server begin ");
            Socket socket;
            while ((socket = serverSocket.accept())!=null){
                logger.info("Client Connection! IP:{} Port:{}",socket.getInetAddress(),socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket,requestHandler,serializer));
            }
            threadPool.shutdown();
        }catch (IOException e){
            logger.info("Server has error in opening:"+e);
        }
    }

    /**
     * 将服务保存在本地的注册表，同时注册到nacos注册中心
     * @param service
     * @param serviceClass
     * @param <T>
     */
    public <T> void publishService(T service, Class<T> serviceClass) {
        if (serializer == null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.addServiceProvider(service, serviceClass);
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        start();
    }
    @Override
    public void setSerializer (CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
