package com.thunder.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡接口
 */
public interface LoadBalancer {

    /**
     * @description 从一系列Instance中选择一个
     * @return [com.alibaba.nacos.api.naming.pojo.Instance]
     *
     */
    Instance select(List<Instance> instances);

}