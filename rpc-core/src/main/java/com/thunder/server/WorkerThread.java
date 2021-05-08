package com.thunder.server;

import com.thunder.entity.RpcRequest;
import com.thunder.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * 实际执行方法调用任务的工作线程
 */
public class WorkerThread implements Runnable{

    private Socket socket;
    private Object service;
    private static final Logger logger = LoggerFactory.getLogger(WorkerThread.class);
    public WorkerThread (Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run () {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest)objectInputStream.readObject();
            //利用反射原理找到远程所需调用的方法
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            //invoke(obj实例对象,obj可变参数)
            Object returnObject = method.invoke(service, rpcRequest.getParameters());
            objectOutputStream.writeObject(RpcResponse.success(returnObject));
            objectOutputStream.flush();
        }catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            logger.info("An error occurred when calling or sending：" + e);
        }
    }
}
