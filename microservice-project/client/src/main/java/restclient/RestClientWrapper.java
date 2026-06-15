package restclient;

import restclient.resource.MultipartFileInputStreamResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO. RestTemplate的包装器，通过serviceName服务名称找到注册的微服务并发送请求
public class RestClientWrapper {

    private final boolean sslEnabled;
    private final RestTemplate restTemplate;

    // TODO. 通过微服务应用的名称来定位并负载均衡
    private final String serviceName;
    private final String serviceBasePath;

    private boolean throwWhenUnavailable;
    private boolean throwOnError;

    private Logger logger;
    private static Pattern pattern = Pattern.compile("No instances available for (.*)");

    public RestClientWrapper(String serviceName, RestTemplate restTemplate) {
        this(serviceName, restTemplate, null, true);
    }

    public RestClientWrapper(String serviceName, RestTemplate restTemplate, boolean sslEnabled) {
        this(serviceName, restTemplate, null, sslEnabled);
    }

    public RestClientWrapper(String serviceName, RestTemplate restTemplate, String serviceBasePath) {
        this(serviceName, restTemplate, serviceBasePath, true);
    }

    public RestClientWrapper(String serviceName, RestTemplate restTemplate, String serviceBasePath, boolean sslEnabled) {
        this.logger = LoggerFactory.getLogger(RestClientWrapper.class);
        this.throwWhenUnavailable = true;
        this.throwOnError = false;
        this.serviceName = serviceName;
        this.restTemplate = restTemplate;
        this.serviceBasePath = serviceBasePath;
        this.sslEnabled = sslEnabled;
    }

    // TODO. Request请求中带有重试次数
    public <T> RestResponse<T> query(RestRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return this.query(request,
                request.getRetryCount(),
                headers,
                (httpHeaders) -> request.getRequestBody() != null ? new HttpEntity(request.getRequestBody(), headers) : new HttpEntity(headers));
    }

    public <T> ResponseEntity<T> query(RestRequest request, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return this.queryInternal(request,
                new ParameterizedTypeReference<T>() {},
                request.getRetryCount(),
                headers,
                (httpHeaders) -> request.getRequestBody() != null ? new HttpEntity(request.getRequestBody(), headers) : new HttpEntity(headers));
    }

    public <T> ResponseEntity<T> query(RestRequest request, ParameterizedTypeReference<T> parameterizedTypeReference) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return this.queryInternal(request,
                parameterizedTypeReference,
                request.getRetryCount(),
                headers,
                (httpHeaders) -> request.getRequestBody() != null ? new HttpEntity(request.getRequestBody(), headers) : new HttpEntity(headers));
    }

    public RestResponse<Object> query(RestRequest request, HttpHeaders headers) {
        return this.query(request,
                request.getRetryCount(),
                headers,
                (httpHeaders) -> request.getRequestBody() != null ? new HttpEntity(request.getRequestBody(), headers) : new HttpEntity(headers));
    }

    public RestResponse<Object> multipartQuery(RestRequest request, String fileParamName, String fileName, InputStream fileResource, MultiValueMap<String, Object> extraParams) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return this.query(request, request.getRetryCount(), headers, (httpHeaders) -> {
            MultiValueMap<String, Object> params = new LinkedMultiValueMap();
            InputStreamResource is = new MultipartFileInputStreamResource(fileResource, fileName);
            if (request.getRequestBody() != null) {
                params.set(fileParamName == null ? "file" : fileParamName, is);
                if (extraParams != null) {
                    Objects.requireNonNull(params);
                    extraParams.forEach(params::putIfAbsent);
                }
            }

            return new HttpEntity(params, headers);
        });
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public RestClientWrapper setThrowWhenUnavailable(boolean throwWhenUnavailable) {
        this.throwWhenUnavailable = throwWhenUnavailable;
        return this;
    }

    public boolean isThrowWhenUnavailable() {
        return this.throwWhenUnavailable;
    }

    public void setThrowOnError(boolean throwOnError) {
        this.throwOnError = throwOnError;
    }

    public boolean isThrowOnError() {
        return this.throwOnError;
    }

    private <T, B> RestResponse<T> query(RestRequest request,
                                         int remainingAttempts,
                                         HttpHeaders headers,
                                         Function<HttpHeaders, HttpEntity<B>> function) {
        ResponseEntity<RestResponse<T>> responseEntity = this.queryInternal(request,
                new ParameterizedTypeReference<RestResponse<T>>() {},
                remainingAttempts,
                headers,
                function);
        if (null != responseEntity) {
            RestResponse<T> body = (RestResponse)responseEntity.getBody();
            if (body == null) {
                return new RestResponse(responseEntity.getStatusCode().value());
            } else {
                body.setHttpStatus(HttpStatus.valueOf(responseEntity.getStatusCode().value()));
                return body;
            }
        } else {
            return new RestResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // TODO. 根据微服务名称来构建URL请求链接，和特定微服务通讯
    private <T, B> ResponseEntity<T> queryInternal(RestRequest request,
                                                   ParameterizedTypeReference<T> parameterizedTypeReference,
                                                   int remainingAttempts,
                                                   HttpHeaders headers,
                                                   Function<HttpHeaders, HttpEntity<B>> function) {
        String url = null;
        try {
            String requestUrl = request.getRequestUrl();
            requestUrl = requestUrl.startsWith("/") ? requestUrl : "/" + requestUrl;
            String protocol = this.sslEnabled ? "https://" : "http://";
            if (StringUtils.isEmpty(this.serviceBasePath)) {
                url = protocol + this.serviceName + requestUrl;
            } else {
                url = protocol + this.serviceName + "/" + this.serviceBasePath + requestUrl;
            }

            HttpEntity<B> httpEntity = (HttpEntity) function.apply(headers);
            ResponseEntity<T> responseEntity = this.restTemplate.exchange(url, request.getRequestMethod(), httpEntity, parameterizedTypeReference);
            return responseEntity;

        } catch (Exception var12) {
            if (this.isThrowOnError()) {
                throw var12;
            } else {
                if (var12 instanceof IllegalStateException || var12 instanceof ResourceAccessException) {
                    Matcher matcher = pattern.matcher(var12.getMessage());
                    if (matcher.find()) {
                        String serviceNameStr = matcher.group(1);
                        this.logger.warn("Service {} is unavailable", serviceNameStr);
                        return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
                    }
                }

                this.logger.error("Exception calling url '" + url + "': " + var12.getMessage());
                this.logger.debug("", var12);
                if (var12 instanceof HttpStatusCodeException) {
                    HttpStatusCodeException exception = (HttpStatusCodeException)var12;
                    this.logger.error("'" + url + "' return error code " + exception.getStatusCode());
                    return new ResponseEntity(exception.getStatusCode());
                } else {
                    return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
                }
            }
        }
    }
}
