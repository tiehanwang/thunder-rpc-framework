package com.thunder.registry;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 */
public interface ServiceRegistry {

    /**
     * @description 将一个服务注册到注册表
     *
     * @return [void]
     *
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

}