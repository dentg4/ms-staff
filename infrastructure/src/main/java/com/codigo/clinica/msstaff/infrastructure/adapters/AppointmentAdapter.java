package com.codigo.clinica.msstaff.infrastructure.adapters;

import com.codigo.clinica.msstaff.domain.aggregates.constants.Constants;
import com.codigo.clinica.msstaff.domain.aggregates.dto.AppointmentDto;
import com.codigo.clinica.msstaff.domain.aggregates.dto.PatientDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.AppointmentRequest;
import com.codigo.clinica.msstaff.domain.ports.out.AppointmentServiceOut;
import com.codigo.clinica.msstaff.infrastructure.client.ClientMsPatient;
import com.codigo.clinica.msstaff.infrastructure.dao.AppointmentRepository;
import com.codigo.clinica.msstaff.infrastructure.dao.DoctorRepository;
import com.codigo.clinica.msstaff.infrastructure.entity.Appointment;
import com.codigo.clinica.msstaff.infrastructure.entity.Doctor;
import com.codigo.clinica.msstaff.infrastructure.exceptions.ResponseValidationException;
import com.codigo.clinica.msstaff.infrastructure.mapper.AppointmentMapper;
import com.codigo.clinica.msstaff.infrastructure.redis.RedisService;
import com.codigo.clinica.msstaff.infrastructure.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentAdapter implements AppointmentServiceOut {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final ClientMsPatient clientMsPatient;
    private final RedisService redisService;

    @Value("${ms.redis.expiration_time}")
    private int redisExpirationTime;

    @Override
    public AppointmentDto createOut(AppointmentRequest request) {
        Appointment appointment = getEntity(new Appointment(), request,false, null);
        return AppointmentMapper.fromEntity(appointmentRepository.save(appointment));
    }

    @Override
    public Optional<AppointmentDto> findByIdOut(Long id) {
        String redisInfo = redisService.getFromRedis(Constants.REDIS_GET_APPOINTMENT + id);
        AppointmentDto appointmentDto;
        if(redisInfo != null){
            appointmentDto = Util.convertFromString(redisInfo, AppointmentDto.class);
        }else{
            Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResponseValidationException("Appointment not found"));
            appointmentDto = AppointmentMapper.fromEntity(appointment);
            String dataForRedis = Util.convertToString(appointmentDto);
            redisService.saveInRedis(Constants.REDIS_GET_APPOINTMENT + id, dataForRedis, redisExpirationTime);
        }
        return Optional.of(appointmentDto);
    }

    @Override
    public List<AppointmentDto> getAllOut() {
        List<AppointmentDto> dtoList = new ArrayList<>();
        List<Appointment> entities = appointmentRepository.findAll();
        for (Appointment data : entities){
            dtoList.add(AppointmentMapper.fromEntity(data));
        }
        return dtoList;
    }

    @Override
    public AppointmentDto updateOut(Long id, AppointmentRequest request) {
        Optional<Appointment> extractedData = appointmentRepository.findById(id);
        if(extractedData.isPresent()){
            Appointment appointment = getEntity(extractedData.get(), request,true, id);
            return AppointmentMapper.fromEntity(appointmentRepository.save(appointment));
        }else {
            throw new ResponseValidationException("Appointment not found.");
        }
    }

    @Override
    public AppointmentDto deleteOut(Long id) {
        Optional<Appointment> extractedData = appointmentRepository.findById(id);
        if(extractedData.isPresent()){
            extractedData.get().setStatus(0);
            extractedData.get().setDeletedBy(Constants.USU_ADMIN);
            extractedData.get().setDeletedOn(getTimestamp());
            return AppointmentMapper.fromEntity(appointmentRepository.save(extractedData.get()));
        }else {
            throw new ResponseValidationException("Appointment not found.");
        }
    }

    // Support Methods
    private Appointment getEntity(Appointment entity, AppointmentRequest appointmentRequest, boolean updateIf, Long id){
        entity.setDate(appointmentRequest.getDate());
        entity.setDuration(appointmentRequest.getDuration());

        PatientDto patient = Util.validateResponse(clientMsPatient.getPatientById(appointmentRequest.getPatient()));
        entity.setPatient(patient.getIdPatient());

        Doctor doctor = doctorRepository.findById(appointmentRequest.getDoctor())
                .orElseThrow(() -> new ResponseValidationException("Doctor not found"));
        entity.setDoctor(doctor);

        if (updateIf) {
            entity.setId(id);
            entity.setUpdatedBy(Constants.USU_ADMIN);
            entity.setUpdatedOn(getTimestamp());
        } else {
            entity.setStatus(Constants.STATUS_ACTIVE);
            entity.setCreatedBy(Constants.USU_ADMIN);
            entity.setCreatedOn(getTimestamp());
        }
        return entity;
    }

    private Timestamp getTimestamp(){
        long currenTime = System.currentTimeMillis();
        return new Timestamp(currenTime);
    }
}
