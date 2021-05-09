package com.thunder.test;

import com.thunder.api.HelloObject;
import com.thunder.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServiceImpl implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello (HelloObject object) {
        logger.info("accept!,{}",object.getMessage());
        return "this is the value:id = "+object.getId();
    }
}
