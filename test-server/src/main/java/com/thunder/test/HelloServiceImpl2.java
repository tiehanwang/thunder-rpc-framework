package com.thunder.test;

import com.thunder.api.HelloObject;
import com.thunder.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServiceImpl2 implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl2.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("accept msg:{}", object.getMessage());
        return "socket service";
    }
}