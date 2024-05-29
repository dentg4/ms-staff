package com.codigo.clinica.msstaff.domain.aggregates.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DoctorRequest {
    private String name;
    private String surname;
    private String identificationType;
    private String identificationNumber;
    private String cmp;
    private String speciality;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private String clinic;
}
