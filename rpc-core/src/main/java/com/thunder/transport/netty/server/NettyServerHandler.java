package com.thunder.transport.netty.server;

import com.thunder.handler.RequestHandler;
import com.thunder.entity.RpcRequest;

import com.thunder.util.ThreadPoolFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * Netty服务端处理器
 * Netty中处理从客户端传来的RpcRequest
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private RequestHandler requestHandler;


    public NettyServerHandler(){
        requestHandler = new RequestHandler();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg){
        try{
            if(msg.getHeartBeat()){
                logger.info("receive client heartbeat");
                return;
            }
            logger.info("server get request:{}", msg);
            Object response = requestHandler.handle(msg);
            if(ctx.channel().isActive() && ctx.channel().isWritable()) {
                //注意这里的通道是workGroup中的，而NettyServer中创建的是bossGroup的，不要混淆
                ctx.writeAndFlush(response);
            }else {
                logger.error("通道不可写");
            }
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        logger.error("处理过程调用时有错误发生：");
        cause.printStackTrace();
        ctx.close();
    }
}