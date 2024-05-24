package com.codigo.clinica.msstaff.infraestrucrure.adapters;

import com.codigo.clinica.msstaff.domain.aggregates.dto.ClinicDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.ClinicRequest;
import com.codigo.clinica.msstaff.domain.ports.out.ClinicServiceOut;
import com.codigo.clinica.msstaff.infraestrucrure.client.ClientSunat;
import com.codigo.clinica.msstaff.infraestrucrure.dao.ClinicRepository;
import com.codigo.clinica.msstaff.infraestrucrure.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClinicAddapter implements ClinicServiceOut {

    private final ClinicRepository clinicRepository;
    private final RedisService redisService;
    private final ClientSunat clientSunat;

    @Value("${ms.redis.expiration_time}")
    private int redisExpirationTime;

    @Value("${token.api_peru}")
    private String tokenSunat;

    @Override
    public ClinicDto createOut(ClinicRequest request) {
        return null;
    }

    @Override
    public Optional<ClinicDto> findByIdOut(Long id) {
        return Optional.empty();
    }

    @Override
    public List<ClinicDto> getAllOut() {
        return List.of();
    }

    @Override
    public ClinicDto updateOut(Long id, ClinicRequest request) {
        return null;
    }

    @Override
    public ClinicDto deleteOut(Long id) {
        return null;
    }
}
