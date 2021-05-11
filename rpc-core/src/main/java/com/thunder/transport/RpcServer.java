package com.thunder.transport;

import com.thunder.serializer.CommonSerializer;

/**
 *
 *
 * @description 服务端类通过接口
 */
public interface RpcServer {

    void start();

    void setSerializer(CommonSerializer serializer);

    /**
     * @description 向Nacos注册服务
     *
     *
     *
     */
    <T> void publishService(Object service, Class<T> serviceClass);
}