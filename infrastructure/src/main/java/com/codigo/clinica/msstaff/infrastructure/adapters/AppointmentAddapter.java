package com.codigo.clinica.msstaff.infrastructure.adapters;

import com.codigo.clinica.msstaff.domain.aggregates.dto.AppointmentDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.AppointmentRequest;
import com.codigo.clinica.msstaff.domain.ports.out.AppointmentServiceOut;
import com.codigo.clinica.msstaff.infrastructure.dao.AppointmentRepository;
import com.codigo.clinica.msstaff.infrastructure.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentAddapter implements AppointmentServiceOut {

    private final AppointmentRepository appointmentRepository;
    private final RedisService redisService;

    @Value("${ms.redis.expiration_time}")
    private int redisExpirationTime;

    @Override
    public AppointmentDto createOut(AppointmentRequest request) {
        return null;
    }

    @Override
    public Optional<AppointmentDto> findByIdOut(Long id) {
        return Optional.empty();
    }

    @Override
    public List<AppointmentDto> getAllOut() {
        return List.of();
    }

    @Override
    public AppointmentDto updateOut(Long id, AppointmentRequest request) {
        return null;
    }

    @Override
    public AppointmentDto deleteOut(Long id) {
        return null;
    }
}
