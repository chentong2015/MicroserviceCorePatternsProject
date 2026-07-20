package com.springcloud.openfeign.retryer;

@RetryableFeignClient(name = "retryable-client")
public interface RetryableFeignClientInterface {
    
}
