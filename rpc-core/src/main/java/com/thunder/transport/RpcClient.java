package com.thunder.transport;

import com.thunder.entity.RpcRequest;
import com.thunder.serializer.CommonSerializer;

/**
 * 客户端类通用接口
 */
public interface RpcClient {
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;
    Object sendRequest(RpcRequest rpcRequest);
}
