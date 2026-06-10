package com.springcloud.openfeign.retryer;

import feign.RetryableException;
import feign.Retryer;
import org.springframework.stereotype.Component;

// 在Spring Context容器中注入一个Retryer bean
@Component
public class NativeRetryer implements feign.Retryer {

    @Override
    public void continueOrPropagate(RetryableException e) {
    }

    @Override
    public Retryer clone() {
        return null;
    }
}
