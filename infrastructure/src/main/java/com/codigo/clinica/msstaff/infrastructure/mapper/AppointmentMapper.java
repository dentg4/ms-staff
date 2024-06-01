package com.codigo.clinica.msstaff.infrastructure.mapper;

import com.codigo.clinica.msstaff.domain.aggregates.dto.AppointmentDto;
import com.codigo.clinica.msstaff.infrastructure.entity.Appointment;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AppointmentMapper {
    private AppointmentMapper() {
    }

    public static AppointmentDto fromEntity(Appointment entity) {
        return AppointmentDto.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .duration(entity.getDuration())
                .doctorId(entity.getDoctor().getId())
                .doctor(entity.getDoctor()==null ? null : DoctorMapper.fromEntity(entity.getDoctor()))
                .patientId(entity.getPatient())
                .status(entity.getStatus())
                .createdBy(entity.getCreatedBy())
                .createOn(entity.getCreatedOn())
                .updatedBy(entity.getUpdatedBy())
                .updatedOn(entity.getUpdatedOn())
                .deletedBy(entity.getDeletedBy())
                .deletedOn(entity.getDeletedOn())
                .build();
    }

    public static  <T, R> List<R> mapList(List<T> list, Function<T, R> mapper) {

        return list != null
                ? list.stream().map(mapper).toList()
                : Collections.emptyList();
    }
}
