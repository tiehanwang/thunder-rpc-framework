package com.thunder.transport.socket.server;

import com.thunder.handler.RequestHandler;
import com.thunder.enumeration.RpcError;
import com.thunder.exception.RpcException;
import com.thunder.hook.ShutdownHook;
import com.thunder.provider.ServiceProvider;
import com.thunder.provider.ServiceProviderImpl;
import com.thunder.registry.NacosServiceRegistry;
import com.thunder.registry.ServiceRegistry;
import com.thunder.serializer.CommonSerializer;
import com.thunder.transport.AbstractRpcServer;
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
public class SocketServer extends AbstractRpcServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);


    private final ExecutorService threadPool;
    private final RequestHandler requestHandler = new RequestHandler();

    private final CommonSerializer serializer;

    public SocketServer(String host, int port){
        this(host, port, DEFAULT_SERIALIZER);
    }

    public SocketServer(String host, int port, Integer serializerCode){
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
        serializer = CommonSerializer.getByCode(serializerCode);
        //创建线程池
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        //自动注册服务
        scanServices();
    }
    /**
     * 服务启动
     */
    public void start(){
        if (serializer == null){
            logger.error("not set serializer!");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }

        try(ServerSocket serverSocket = new ServerSocket()){
            serverSocket.bind(new InetSocketAddress(host, port));
            logger.info("Server begin ");
            //添加钩子，服务端关闭时会注销服务
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
            while ((socket = serverSocket.accept())!=null){
                logger.info("Client Connection! IP:{} Port:{}",socket.getInetAddress(),socket.getPort());
                threadPool.execute(new SocketRequestHandlerThread(socket,requestHandler,serializer));
            }
            threadPool.shutdown();
        }catch (IOException e){
            logger.info("Server has error in opening:"+e);
        }
    }

}
