package com.codigo.clinica.msstaff.domain.aggregates.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClinicDto {
    private Long id;
    private String name;
    private String identificationType;
    private String identificationNumber;
    private String phone;
    private String email;
    private String address;
    private String website;
    private Integer status;
    private String createdBy;
    private Timestamp createOn;
    private String updatedBy;
    private Timestamp updatedOn;
    private String deletedBy;
    private Timestamp deletedOn;
    private List<DoctorDto> doctors;
}
