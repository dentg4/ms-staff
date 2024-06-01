package com.codigo.clinica.msstaff.infrastructure.client;

import com.codigo.clinica.msstaff.domain.aggregates.dto.PatientDto;
import com.codigo.clinica.msstaff.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-patient", configuration = FeignConfig.class)
public interface ClientMsPatient {

    @GetMapping("/api/v1/ms-patient/patient/find/{id}")
    ResponseEntity<PatientDto> getPatientById(@PathVariable Long id);
}
