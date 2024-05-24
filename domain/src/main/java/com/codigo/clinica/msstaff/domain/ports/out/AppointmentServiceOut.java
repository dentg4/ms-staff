package com.codigo.clinica.msstaff.domain.ports.out;

import com.codigo.clinica.msstaff.domain.aggregates.dto.AppointmentDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.AppointmentRequest;

import java.util.List;
import java.util.Optional;

public interface AppointmentServiceOut {
    AppointmentDto createOut(AppointmentRequest request);
    Optional<AppointmentDto> findByIdOut(Long id);
    List<AppointmentDto> getAllOut();
    AppointmentDto updateOut(Long id, AppointmentRequest request);
    AppointmentDto deleteOut(Long id);
}
