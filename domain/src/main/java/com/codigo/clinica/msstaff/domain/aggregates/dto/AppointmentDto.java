package com.codigo.clinica.msstaff.domain.aggregates.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentDto {
    private Long id;
    private Timestamp date;
    private Integer duration;
    private Integer status;
    private String createdBy;
    private Timestamp createOn;
    private String updatedBy;
    private Timestamp updatedOn;
    private String deletedBy;
    private Timestamp deletedOn;
    private Long patientId;
    private Long doctorId;
    private PatientDto patient;
    private DoctorDto doctor;
}
