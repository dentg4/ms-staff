package com.codigo.clinica.msstaff.domain.aggregates.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClinicRequest {

    @NotBlank(message = "El campo número de identificación es necesario.")
    private String identificationNumber;

    @NotBlank(message = "El campo teléfono es necesario.")
    private String phone;

    @NotBlank(message = "El campo email es necesario.")
    private String email;

    @NotBlank(message = "El campo website es necesario.")
    private String website;
}
