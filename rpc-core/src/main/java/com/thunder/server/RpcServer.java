package com.thunder.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import com.thunder.server.WorkerThread;

/**
 * 进行远程调用连接的服务端
 */
public class RpcServer {
    private final ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    public RpcServer () {
        int corePoolSize = 5;
        int maxPoolSize = 50;
        long keepAliveTime = 60;

        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize,maxPoolSize,keepAliveTime,TimeUnit.SECONDS,workingQueue,threadFactory);
    }

    /**
     * 服务注册
     */
    public void register(Object service,int port){
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server begin ");
            Socket socket;
            while ((socket = serverSocket.accept())!=null){
                logger.info("Client Connection！IP:"+socket.getInetAddress());
                threadPool.execute(new WorkerThread(socket,service));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
