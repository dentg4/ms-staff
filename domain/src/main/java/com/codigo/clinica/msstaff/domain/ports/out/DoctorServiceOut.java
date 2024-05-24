package com.codigo.clinica.msstaff.domain.ports.out;

import com.codigo.clinica.msstaff.domain.aggregates.dto.DoctorDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.DoctorRequest;

import java.util.List;
import java.util.Optional;

public interface DoctorServiceOut {
    DoctorDto createOut(DoctorRequest request);
    Optional<DoctorDto> findByIdOut(Long id);
    List<DoctorDto> getAllOut();
    DoctorDto updateOut(Long id, DoctorRequest request);
    DoctorDto deleteOut(Long id);
}
