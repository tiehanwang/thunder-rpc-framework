package com.thunder.test;

import com.thunder.annotation.Service;
import com.thunder.api.ByeService;

@Service
public class ByeServiceImpl implements ByeService {

    @Override
    public String bye(String name) {
        return "bye," + name;
    }

}