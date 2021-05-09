package com.thunder.registry;

/**
 * 服务注册表通用接口
 */
public interface ServiceRegistry {


    /**
     * 将一个服务注册进注册表
     * @param service 待注册
     * @param <T> 服务实体类
     */
    <T> void register(T service);

    /**
     * 根据服务名称返回服务实体
     * @param serviceName 服务名称
     * @return 服务实体
     */
    Object getService(String serviceName);
}
