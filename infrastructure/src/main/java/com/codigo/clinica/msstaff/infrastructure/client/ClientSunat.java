package com.codigo.clinica.msstaff.infrastructure.client;

import com.codigo.clinica.msstaff.domain.aggregates.dto.SunatDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "client-sunat", url = "https://api.apis.net.pe/v2/sunat/ruc/")
public interface ClientSunat {
    @GetMapping("/full")
    SunatDto getInfoSunat(@RequestParam("numero") String number, @RequestHeader("Authorization") String authorization);
}
