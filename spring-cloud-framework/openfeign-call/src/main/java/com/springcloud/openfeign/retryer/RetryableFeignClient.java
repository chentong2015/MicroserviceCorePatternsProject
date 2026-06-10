package com.springcloud.openfeign.retryer;

import org.springframework.cloud.openfeign.FeignClient;

import java.lang.annotation.*;

// 封装OpenFeign的配置和参数的使用
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@FeignClient
@Documented
public @interface RetryableFeignClient {

    String contextId() default "";

    String name() default "";

    String qualifier() default "";

    String url() default "";

    boolean decode404() default false;

    Class<?>[] configuration() default {};

    Class<?> fallback() default void.class;

    Class<?> fallbackFactory() default void.class;

    String path() default "";

    boolean primary() default true;
}
