package com.thunder.handler;

import com.thunder.entity.RpcRequest;
import com.thunder.entity.RpcResponse;
import com.thunder.enumeration.ResponseCode;
import com.thunder.provider.ServiceProvider;
import com.thunder.provider.ServiceProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * 实际执行方法的处理器
 */
public class RequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handle(RpcRequest rpcRequest){
        Object result = null;
        //从服务端本地注册表中获取服务实体
        Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest,service);
    }
    private Object invokeTargetMethod(RpcRequest rpcRequest,Object service){
        Object result;
        try{
            //getClass()获取的是实例对象的类型
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            logger.info("service:{} success method:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        }catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            logger.info("use or send has error:"+e);
            return RpcResponse.fail(ResponseCode.NOT_FOUND_METHOD, rpcRequest.getRequestId());
        }
        //方法调用成功
        return RpcResponse.success(result, rpcRequest.getRequestId());
    }
}
