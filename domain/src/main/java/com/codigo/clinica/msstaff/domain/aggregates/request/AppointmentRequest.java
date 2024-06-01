package com.codigo.clinica.msstaff.domain.aggregates.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class AppointmentRequest {

    @NotNull(message = "El campo fecha es necesario.")
    private Timestamp date;

    @NotNull(message = "El campo duración es necesario.")
    private Integer duration;

    @NotNull(message = "El campo doctor es necesario.")
    private Long doctor;

    @NotNull(message = "El campo paciente es necesario.")
    private Long patient;
}
