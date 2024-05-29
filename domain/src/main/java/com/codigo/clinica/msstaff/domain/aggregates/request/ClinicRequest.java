package com.codigo.clinica.msstaff.domain.aggregates.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClinicRequest {
    private String identificationNumber;
    private String phone;
    private String email;
    private String website;
}
