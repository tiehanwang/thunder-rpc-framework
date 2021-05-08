package com.thunder.entity;

import com.thunder.enumeration.ResponseCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class RpcResponse<T> implements Serializable {
    //状态码
    private Integer statusCode;
    //响应状态码对应的消息
    private String message;
    //成功时数据
    private T data;

    /**
     * 成功时服务端返回对象
     * @param data
     * @param <T>
     * @return
     */
    public static <T> RpcResponse<T> success(T data){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    /**
     * 失败时服务端返回对象
     * @param code
     * @param <T>
     * @return
     */
    public static <T> RpcResponse<T> fail(ResponseCode code){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}
