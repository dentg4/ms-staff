package com.codigo.clinica.msstaff.domain.ports.out;

import com.codigo.clinica.msstaff.domain.aggregates.dto.ClinicDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.ClinicRequest;

import java.util.List;
import java.util.Optional;

public interface ClinicServiceOut {
    ClinicDto createOut(ClinicRequest request);
    Optional<ClinicDto> findByIdOut(Long id);
    List<ClinicDto> getAllOut();
    ClinicDto updateOut(Long id, ClinicRequest request);
    ClinicDto deleteOut(Long id);
}
