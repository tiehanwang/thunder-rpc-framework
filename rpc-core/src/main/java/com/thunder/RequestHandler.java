package com.thunder;

import com.thunder.entity.RpcRequest;
import com.thunder.entity.RpcResponse;
import com.thunder.enumeration.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * 实际执行方法的处理器
 */
public class RequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public Object handle(RpcRequest rpcRequest, Object service){
        Object result = null;
        try{
            result = invokeTargetMethod(rpcRequest, service);
            logger.info("service:{}success method:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        }catch (IllegalAccessException | InvocationTargetException e){
            logger.info("error in send or use：" + e);
        }
        return result;
    }
    private Object invokeTargetMethod(RpcRequest rpcRequest,Object service) throws InvocationTargetException, IllegalAccessException{
        Method method = null;
        try{
            //getClass()获取的是实例对象的类型
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        }catch (NoSuchMethodException e){
            logger.info("use or send has error:"+e);
        }
        return method.invoke(service, rpcRequest.getParameters());
    }
}
