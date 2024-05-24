package com.codigo.clinica.msstaff.domain.ports.in;

import com.codigo.clinica.msstaff.domain.aggregates.dto.ClinicDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.ClinicRequest;

import java.util.List;
import java.util.Optional;

public interface ClinicServiceIn {
    ClinicDto createIn(ClinicRequest request);
    Optional<ClinicDto> findByIdIn(Long id);
    List<ClinicDto> getAllIn();
    ClinicDto updateIn(Long id, ClinicRequest request);
    ClinicDto deleteIn(Long id);
}
