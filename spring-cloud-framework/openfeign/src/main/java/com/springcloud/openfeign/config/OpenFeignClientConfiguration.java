package com.springcloud.openfeign.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class OpenFeignClientConfiguration {

    @Autowired
    private Environment environment;

    // All the requests will contain the basic authentication header.
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("username", "password");
    }

    // RequestInterceptor统一拦截请求来完成设置header等相关请求
    // 实现URL基于环境的动态配置
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                String url = template.url();
                if (url.contains("$env")) {
                    url = url.replace("$env", route(template));
                    template.uri(url);
                }
                if (url.startsWith("//")) {
                    url = "http:" + url;
                    template.target(url);
                    template.uri("");
                }
            }

            private CharSequence route(RequestTemplate template) {
                return environment.getProperty("feign.env");
            }
        };
    }

    @Bean
    public MyRouteTargeter getRouteTargeter(Environment environment) {
        return new MyRouteTargeter(environment);
    }
}
