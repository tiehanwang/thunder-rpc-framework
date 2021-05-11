package com.thunder.transport;

import com.thunder.serializer.CommonSerializer;

/**
 *
 *
 * @description 服务端类通过接口
 */
public interface RpcServer {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    void start();

    /**
     * @description 向Nacos注册服务
     *
     *
     *
     */
    <T> void publishService(T service, String serviceName);
}
