package com.codigo.clinica.msstaff.domain.aggregates.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DoctorRequest {

    @NotBlank(message = "El campo número de identificación es necesario.")
    private String identificationNumber;

    @NotBlank(message = "El campo CMP es necesario.")
    private String cmp;

    @NotBlank(message = "El campo especialidad es necesario.")
    private String speciality;

    @NotBlank(message = "El campo género es necesario.")
    private String gender;

    @NotBlank(message = "El campo teléfono es necesario.")
    private String phone;

    @NotBlank(message = "El campo email es necesario.")
    private String email;

    @NotBlank(message = "El campo dirección es necesario.")
    private String address;

    @NotBlank(message = "El campo clinica es necesario.")
    private String clinic;
}
