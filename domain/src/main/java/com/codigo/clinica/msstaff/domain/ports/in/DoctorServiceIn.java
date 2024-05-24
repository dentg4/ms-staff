package com.codigo.clinica.msstaff.domain.ports.in;

import com.codigo.clinica.msstaff.domain.aggregates.dto.DoctorDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.DoctorRequest;

import java.util.List;
import java.util.Optional;

public interface DoctorServiceIn {
    DoctorDto createIn(DoctorRequest request);
    Optional<DoctorDto> findByIdIn(Long id);
    List<DoctorDto> getAllIn();
    DoctorDto updateIn(Long id, DoctorRequest request);
    DoctorDto deleteIn(Long id);
}
