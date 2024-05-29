package com.codigo.clinica.msstaff.domain.aggregates.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoctorDto {
    private Long id;
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
    private String clinicId;

    private Integer status;
    private String createdBy;
    private Timestamp createOn;
    private String updatedBy;
    private Timestamp updatedOn;
    private String deletedBy;
    private Timestamp deletedOn;
}

