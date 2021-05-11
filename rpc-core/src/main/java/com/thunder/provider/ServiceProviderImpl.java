package com.thunder.provider;

import com.thunder.enumeration.RpcError;
import com.thunder.exception.RpcException;
import com.thunder.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceProviderImpl implements ServiceProvider {
    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);
    /**
     * String服务名称（即接口名） Object 服务实体（即实现类的实例对象）
     */
    private final static Map<String,Object> serviceMap = new ConcurrentHashMap<>();
    /**
     * 用来存放服务名称(即接口名）
     */
    private final static Set<String> registeredService = ConcurrentHashMap.newKeySet();

    /**
     * 将一个服务注册进注册表 服务是服务实体（实例对象）
     *
     * @param service 待注册
     */
    @Override
    public <T> void addServiceProvider(T service, String serviceName) {
        if(registeredService.contains(serviceName)){
            return;
        }
        registeredService.add(serviceName);
        serviceMap.put(serviceName, service);
        logger.info("interface：{} register service：{}", service.getClass().getInterfaces(), serviceName);
    }

    /**
     * 根据服务名称返回服务实体
     *
     * @param serviceName 服务名称
     * @return 服务实体
     */
    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if(service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
