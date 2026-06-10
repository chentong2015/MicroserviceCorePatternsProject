package com.springcloud.openfeign.client;

import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;

public class MyCallback implements MyCallbackInterface {

    @Override
    public String getHello() {
        throw new NoFallbackAvailableException("Boom!", new RuntimeException());
    }

    @Override
    public String getException() {
        return "fixed response";
    }
}
