package com.thunder.registry;

import java.net.InetSocketAddress;

/**
 * 服务注册表通用接口
 */
public interface ServiceRegistry {

    /**
     * @description 将一个服务注册到注册表
     *
     * @return [void]
     *
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

    /**
     * @description 根据服务名查找服务实体
     *
     * @return [java.net.InetSocketAddress] 服务实体
     *
     */
    InetSocketAddress lookupService(String serviceName);

}