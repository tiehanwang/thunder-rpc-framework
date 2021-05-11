package com.thunder.registry;

import java.net.InetSocketAddress;

/**
 * 服务发现接口
 */
public interface ServiceDiscovery {
    /**
     * @description 根据服务名称查找服务端地址
     *
     * @return [java.net.InetSocketAddress]
     *
     */
    InetSocketAddress lookupService(String serviceName);
}