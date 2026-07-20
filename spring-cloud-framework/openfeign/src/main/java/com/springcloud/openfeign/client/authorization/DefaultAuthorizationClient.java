package com.springcloud.openfeign.client.authorization;

public interface DefaultAuthorizationClient {

    boolean checkPermission(String jwtToken, String resource, String action);
}
