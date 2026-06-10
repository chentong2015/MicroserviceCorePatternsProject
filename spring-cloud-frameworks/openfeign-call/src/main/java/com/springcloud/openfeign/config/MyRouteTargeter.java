package com.springcloud.openfeign.config;

import feign.Feign;
import feign.Request;
import feign.RequestTemplate;
import feign.Target;
import org.springframework.cloud.openfeign.FeignClientFactoryBean;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.cloud.openfeign.Targeter;
import org.springframework.core.env.Environment;

public class MyRouteTargeter implements Targeter {

    private Environment environment;

    public MyRouteTargeter(Environment environment) {
        this.environment = environment;
    }

    // 服务名以本字符串结尾的，会被置换为实现定位到环境
    public static final String CLUSTER_ID_SUFFIX = "env";
    // 新的服务名后缀名称需要从Environment中动态的获取
    public static final String CLUSTER_ID_SUFFIX_NEW = "env-new";

    @Override
    public <T> T target(FeignClientFactoryBean factory,
                        Feign.Builder feign,
                        FeignContext context,
                        Target.HardCodedTarget<T> target) {
        return feign.target(new RouteTarget<>(target));
    }

    public static class RouteTarget<T> implements Target<T> {
        private Target<T> realTarget;

        public RouteTarget(Target<T> realTarget) {
            super();
            this.realTarget = realTarget;
        }

        @Override
        public Class<T> type() {
            return realTarget.type();
        }

        @Override
        public String name() {
            return realTarget.name();
        }

        @Override
        public String url() {
            String url = realTarget.url();
            if (url.endsWith(CLUSTER_ID_SUFFIX)) {
                url = url.replace(CLUSTER_ID_SUFFIX, CLUSTER_ID_SUFFIX_NEW);
            }
            return url;
        }

        @Override
        public Request apply(RequestTemplate input) {
            if (input.url().indexOf("http") != 0) {
                input.target(url());
            }
            return input.request();
        }
    }
}
