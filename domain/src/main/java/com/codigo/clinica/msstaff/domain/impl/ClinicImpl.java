package com.codigo.clinica.msstaff.domain.impl;

import com.codigo.clinica.msstaff.domain.aggregates.dto.ClinicDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.ClinicRequest;
import com.codigo.clinica.msstaff.domain.ports.in.ClinicServiceIn;
import com.codigo.clinica.msstaff.domain.ports.out.ClinicServiceOut;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClinicImpl implements ClinicServiceIn {

    private final ClinicServiceOut clinicServiceOut;

    @Override
    public ClinicDto createIn(ClinicRequest request) {
        return clinicServiceOut.createOut(request);
    }

    @Override
    public Optional<ClinicDto> findByIdIn(Long id) {
        return clinicServiceOut.findByIdOut(id);
    }

    @Override
    public List<ClinicDto> getAllIn() {
        return clinicServiceOut.getAllOut();
    }

    @Override
    public ClinicDto updateIn(Long id, ClinicRequest request) {
        return clinicServiceOut.updateOut(id, request);
    }

    @Override
    public ClinicDto deleteIn(Long id) {
        return clinicServiceOut.deleteOut(id);
    }
}
