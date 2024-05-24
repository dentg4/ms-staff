package com.codigo.clinica.msstaff.domain.ports.in;

import com.codigo.clinica.msstaff.domain.aggregates.dto.AppointmentDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.AppointmentRequest;

import java.util.List;
import java.util.Optional;

public interface AppointmentServiceIn {
    AppointmentDto createIn(AppointmentRequest request);
    Optional<AppointmentDto> findByIdIn(Long id);
    List<AppointmentDto> getAllIn();
    AppointmentDto updateIn(Long id, AppointmentRequest request);
    AppointmentDto deleteIn(Long id);
}
