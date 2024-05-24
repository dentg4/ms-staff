package com.codigo.clinica.msstaff.domain.aggregates.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class AppointmentRequest {
    private Timestamp date;
    private Integer duration;
    private Long doctor;
    //private Long patient;
}
