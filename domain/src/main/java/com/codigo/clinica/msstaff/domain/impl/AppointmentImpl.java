package com.codigo.clinica.msstaff.domain.impl;

import com.codigo.clinica.msstaff.domain.aggregates.dto.AppointmentDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.AppointmentRequest;
import com.codigo.clinica.msstaff.domain.ports.in.AppointmentServiceIn;
import com.codigo.clinica.msstaff.domain.ports.out.AppointmentServiceOut;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentImpl implements AppointmentServiceIn {

    private final AppointmentServiceOut appointmentServiceOut;

    @Override
    public AppointmentDto createIn(AppointmentRequest request) {
        return appointmentServiceOut.createOut(request);
    }

    @Override
    public Optional<AppointmentDto> findByIdIn(Long id) {
        return appointmentServiceOut.findByIdOut(id);
    }

    @Override
    public List<AppointmentDto> getAllIn() {
        return appointmentServiceOut.getAllOut();
    }

    @Override
    public AppointmentDto updateIn(Long id, AppointmentRequest request) {
        return appointmentServiceOut.updateOut(id, request);
    }

    @Override
    public AppointmentDto deleteIn(Long id) {
        return appointmentServiceOut.deleteOut(id);
    }
}
