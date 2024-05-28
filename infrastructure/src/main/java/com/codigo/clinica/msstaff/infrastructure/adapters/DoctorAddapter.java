package com.codigo.clinica.msstaff.infrastructure.adapters;

import com.codigo.clinica.msstaff.domain.aggregates.dto.DoctorDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.DoctorRequest;
import com.codigo.clinica.msstaff.domain.ports.out.DoctorServiceOut;
import com.codigo.clinica.msstaff.infrastructure.client.ClientReniec;
import com.codigo.clinica.msstaff.infrastructure.dao.DoctorRepository;
import com.codigo.clinica.msstaff.infrastructure.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorAddapter implements DoctorServiceOut {

    private final DoctorRepository doctorRepository;
    private final RedisService redisService;
    private final ClientReniec clientReniec;

    @Value("${ms.redis.expiration_time}")
    private int redisExpirationTime;

    @Value("${token.api_peru}")
    private String tokenReniec;

    @Override
    public DoctorDto createOut(DoctorRequest request) {
        return null;
    }

    @Override
    public Optional<DoctorDto> findByIdOut(Long id) {
        return Optional.empty();
    }

    @Override
    public List<DoctorDto> getAllOut() {
        return List.of();
    }

    @Override
    public DoctorDto updateOut(Long id, DoctorRequest request) {
        return null;
    }

    @Override
    public DoctorDto deleteOut(Long id) {
        return null;
    }
}
