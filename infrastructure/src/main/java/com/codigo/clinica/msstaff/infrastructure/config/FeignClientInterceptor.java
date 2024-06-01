package com.codigo.clinica.msstaff.infrastructure.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeignClientInterceptor implements RequestInterceptor {

    @Value("${token.api_peru}")
    private String tokenReniec;

    private static final String AUTH_HEADER="Authorization";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String url = requestTemplate.feignTarget().url();
        if(url.contains("api.apis.net.pe")){
            requestTemplate.header(AUTH_HEADER, "Bearer " + tokenReniec);
        }else{
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                String authorization = requestAttributes.getRequest().getHeader(AUTH_HEADER);
                if(authorization != null && authorization.startsWith("Bearer ")){
                    requestTemplate.header(AUTH_HEADER, authorization);
                }
            }
        }
    }
}
