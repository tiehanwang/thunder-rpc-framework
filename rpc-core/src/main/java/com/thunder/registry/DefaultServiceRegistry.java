package com.thunder.registry;

import com.thunder.enumeration.RpcError;
import com.thunder.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultServiceRegistry implements ServiceRegistry{
    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceRegistry.class);
    /**
     * String服务名称（即接口名） Object 服务实体（即实现类的实例对象）
     */
    private final static Map<String,Object> serviceMap = new ConcurrentHashMap<>();
    /**
     * 用来存放实现类的名称，Set存取更高效，存放实现类名称相比存放接口名称占的空间更小，因为一个实现类可能实现了多个接口，查找效率也会更高
     */
    private final static Set<String> registeredService = ConcurrentHashMap.newKeySet();

    /**
     * 将一个服务注册进注册表 服务是服务实体（实例对象）
     *
     * @param service 待注册
     */
    @Override
    public synchronized <T> void register (T service) {
        String serviceImplName = service.getClass().getCanonicalName();
        if(registeredService.contains(serviceImplName)){
            return;
        }
        registeredService.add(serviceImplName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if(interfaces.length == 0){
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class<?> anInterface : interfaces) {
            serviceMap.put(anInterface.getCanonicalName(),service);
        }
        logger.info("Interface:{} Register:{}",interfaces,serviceImplName);
    }

    /**
     * 根据服务名称返回服务实体
     *
     * @param serviceName 服务名称
     * @return 服务实体
     */
    @Override
    public synchronized Object getService (String serviceName) {
        Object service = serviceMap.get(serviceName);
        if(service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
