package com.codigo.clinica.msstaff.infrastructure.client;

import com.codigo.clinica.msstaff.domain.aggregates.request.TokenRequest;
import com.codigo.clinica.msstaff.domain.aggregates.response.TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-security")
public interface ClientMsSecurity {

    @PostMapping("api/v1/auth/validatetoken")
    TokenResponse validateToken(@RequestBody TokenRequest tokenRequest);
}
