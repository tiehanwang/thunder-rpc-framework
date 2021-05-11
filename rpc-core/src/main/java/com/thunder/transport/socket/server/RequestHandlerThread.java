package com.thunder.transport.socket.server;

import com.thunder.handler.RequestHandler;
import com.thunder.entity.RpcRequest;
import com.thunder.serializer.CommonSerializer;
import com.thunder.transport.socket.util.ObjectReader;
import com.thunder.transport.socket.util.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * 处理客户端RpcRequest的工作线程
 */
public class RequestHandlerThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private CommonSerializer serializer;
    public RequestHandlerThread(Socket socket, RequestHandler requestHandler,CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serializer = serializer;
    }
    @Override
    public void run () {
        try(InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream()) {
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
            Object response = requestHandler.handle(rpcRequest);
            ObjectWriter.writeObject(outputStream, response, serializer);
        }catch (IOException e){
            logger.info("send or use error"+e);
        }
    }
}
