package com.codigo.clinica.msstaff.infrastructure.adapters;

import com.codigo.clinica.msstaff.domain.aggregates.constants.Constants;
import com.codigo.clinica.msstaff.domain.aggregates.dto.DoctorDto;
import com.codigo.clinica.msstaff.domain.aggregates.dto.ReniecDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.DoctorRequest;
import com.codigo.clinica.msstaff.domain.ports.out.DoctorServiceOut;
import com.codigo.clinica.msstaff.infrastructure.client.ClientReniec;
import com.codigo.clinica.msstaff.infrastructure.dao.ClinicRepository;
import com.codigo.clinica.msstaff.infrastructure.dao.DoctorRepository;
import com.codigo.clinica.msstaff.infrastructure.entity.Clinic;
import com.codigo.clinica.msstaff.infrastructure.entity.Doctor;
import com.codigo.clinica.msstaff.infrastructure.exceptions.ResponseValidationException;
import com.codigo.clinica.msstaff.infrastructure.mapper.DoctorMapper;
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
public class DoctorAdapter implements DoctorServiceOut {

    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository;
    private final RedisService redisService;
    private final ClientReniec clientReniec;

    @Value("${ms.redis.expiration_time}")
    private int redisExpirationTime;

    @Value("${token.api_peru}")
    private String tokenReniec;

    @Override
    public DoctorDto createOut(DoctorRequest request) {
        Doctor doctor = getEntity(new Doctor(), request,false, null);
        return DoctorMapper.fromEntity(doctorRepository.save(doctor));
    }

    @Override
    public Optional<DoctorDto> findByIdOut(Long id) {
        String redisInfo = redisService.getFromRedis(Constants.REDIS_GET_DOCTOR + id);
        DoctorDto doctorDto;
        if(redisInfo != null){
            doctorDto = Util.convertFromString(redisInfo, DoctorDto.class);
        }else{
            Doctor doctor = doctorRepository.findById(id).orElseThrow(()->new ResponseValidationException("Doctor not found"));
            doctorDto = DoctorMapper.fromEntity(doctor);
            String dataForRedis = Util.convertToString(doctorDto);
            redisService.saveInRedis(Constants.REDIS_GET_DOCTOR + id, dataForRedis, redisExpirationTime);
        }
        return Optional.of(doctorDto);
    }

    @Override
    public List<DoctorDto> getAllOut() {
        List<DoctorDto> dtoList = new ArrayList<>();
        List<Doctor> entities = doctorRepository.findAll();
        for (Doctor data : entities){
            dtoList.add(DoctorMapper.fromEntity(data));
        }
        return dtoList;
    }

    @Override
    public DoctorDto updateOut(Long id, DoctorRequest request) {
        Optional<Doctor> extractedData = doctorRepository.findById(id);
        if(extractedData.isPresent()){
            Doctor doctor = getEntity(extractedData.get(), request,true, id);
            return DoctorMapper.fromEntity(doctorRepository.save(doctor));
        }else {
            throw new ResponseValidationException("Doctor not found.");
        }
    }

    @Override
    public DoctorDto deleteOut(Long id) {
        Optional<Doctor> extractedData = doctorRepository.findById(id);
        if(extractedData.isPresent()){
            extractedData.get().setStatus(0);
            extractedData.get().setDeletedBy(Constants.USU_ADMIN);
            extractedData.get().setDeletedOn(getTimestamp());
            return DoctorMapper.fromEntity(doctorRepository.save(extractedData.get()));
        }else {
            throw new ResponseValidationException("Doctor not found.");
        }
    }

    // Support Methods
    private Doctor getEntity(Doctor entity, DoctorRequest doctorRequest, boolean updateIf, Long id){
        ReniecDto reniecDto = getReniec(doctorRequest.getIdentificationNumber());

        entity.setIdentificationType(reniecDto.getTipoDocumento());
        entity.setIdentificationNumber(reniecDto.getNumeroDocumento());
        entity.setName(reniecDto.getNombres());
        entity.setSurname(reniecDto.getApellidoMaterno() + " " + reniecDto.getApellidoMaterno());
        entity.setCmp(doctorRequest.getCmp());
        entity.setSpeciality(doctorRequest.getSpeciality());
        entity.setGender(doctorRequest.getGender());
        entity.setPhone(doctorRequest.getPhone());
        entity.setEmail(doctorRequest.getEmail());
        entity.setAddress(doctorRequest.getAddress());

        Clinic clinic = clinicRepository.findByIdentificationNumber(doctorRequest.getClinic()).orElseThrow(() -> {
            throw new ResponseValidationException("Clinic not found");
        });

        entity.setClinic(clinic);

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

    private ReniecDto getReniec(String identificationNumber){
        String authorization = "Bearer " + tokenReniec;
        return clientReniec.getInfoReniec(identificationNumber, authorization);
    }

    private Timestamp getTimestamp(){
        long currenTime = System.currentTimeMillis();
        return new Timestamp(currenTime);
    }
}
