package com.codigo.clinica.msstaff.domain.impl;

import com.codigo.clinica.msstaff.domain.aggregates.dto.DoctorDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.DoctorRequest;
import com.codigo.clinica.msstaff.domain.ports.in.DoctorServiceIn;
import com.codigo.clinica.msstaff.domain.ports.out.DoctorServiceOut;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class DoctorImpl implements DoctorServiceIn {

    private final DoctorServiceOut doctorServiceOut;

    @Override
    public DoctorDto createIn(DoctorRequest request) {
        return doctorServiceOut.createOut(request);
    }

    @Override
    public Optional<DoctorDto> findByIdIn(Long id) {
        return doctorServiceOut.findByIdOut(id);
    }

    @Override
    public List<DoctorDto> getAllIn() {
        return doctorServiceOut.getAllOut();
    }

    @Override
    public DoctorDto updateIn(Long id, DoctorRequest request) {
        return doctorServiceOut.updateOut(id, request);
    }

    @Override
    public DoctorDto deleteIn(Long id) {
        return doctorServiceOut.deleteOut(id);
    }
}
