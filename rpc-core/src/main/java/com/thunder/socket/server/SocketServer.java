package com.thunder.socket.server;

import com.thunder.RequestHandler;
import com.thunder.RpcServer;
import com.thunder.enumeration.RpcError;
import com.thunder.exception.RpcException;
import com.thunder.registry.ServiceRegistry;
import com.thunder.serializer.CommonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;


/**
 * 进行远程调用连接的服务端
 */
public class SocketServer implements RpcServer{

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private static final int corePoolSize = 5;
    private static final int maxPoolSize = 50;
    private static final long keepAliveTime = 60;
    private final ExecutorService threadPool;
    private final ServiceRegistry serviceRegistry;
    private final RequestHandler requestHandler = new RequestHandler();
    private CommonSerializer serializer;
    public SocketServer (ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        /**
         * 阻塞队列
         */
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        /**
         * 线程池
         */
        threadPool = new ThreadPoolExecutor(corePoolSize,maxPoolSize,keepAliveTime,TimeUnit.SECONDS,workingQueue,threadFactory);
    }
    /**
     * 服务启动
     */
    public void start(int port){
        if (serializer == null){
            logger.error("not set serializer!");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("Server begin ");
            Socket socket;
            while ((socket = serverSocket.accept())!=null){
                logger.info("Client Connection! IP:{} Port:{}",socket.getInetAddress(),socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket,requestHandler,serviceRegistry,serializer));
            }
            threadPool.shutdown();
        }catch (IOException e){
            logger.info("Server has error in opening:"+e);
        }
    }

    @Override
    public void setSerializer (CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
