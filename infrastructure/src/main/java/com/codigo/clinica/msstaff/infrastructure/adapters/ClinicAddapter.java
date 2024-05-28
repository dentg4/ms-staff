package com.codigo.clinica.msstaff.infrastructure.adapters;

import com.codigo.clinica.msstaff.domain.aggregates.constants.Constants;
import com.codigo.clinica.msstaff.domain.aggregates.dto.ClinicDto;
import com.codigo.clinica.msstaff.domain.aggregates.dto.SunatDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.ClinicRequest;
import com.codigo.clinica.msstaff.domain.ports.out.ClinicServiceOut;
import com.codigo.clinica.msstaff.infrastructure.client.ClientSunat;
import com.codigo.clinica.msstaff.infrastructure.dao.ClinicRepository;
import com.codigo.clinica.msstaff.infrastructure.entity.Clinic;
import com.codigo.clinica.msstaff.infrastructure.mapper.ClinicMapper;
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
        Clinic clinic = getEntity(new Clinic(), request,false, null);
        return ClinicMapper.fromEntity(clinicRepository.save(clinic));
    }

    @Override
    public Optional<ClinicDto> findByIdOut(Long id) {
        String redisInfo = redisService.getFromRedis(Constants.REDIS_GET_CLINIC + id);
        ClinicDto clinicDto;
        if(redisInfo != null){
            clinicDto = Util.convertFromString(redisInfo, ClinicDto.class);
        }else{
            clinicDto = ClinicMapper.fromEntity(clinicRepository.findById(id).get());
            String dataForRedis = Util.convertToString(clinicDto);
            redisService.saveInRedis(Constants.REDIS_GET_CLINIC + id, dataForRedis, redisExpirationTime);
        }
        return Optional.of(clinicDto);
    }

    @Override
    public List<ClinicDto> getAllOut() {
        List<ClinicDto> dtoList = new ArrayList<>();
        List<Clinic> entities = clinicRepository.findAll();
        for (Clinic data : entities){
            dtoList.add(ClinicMapper.fromEntity(data));
        }
        return dtoList;
    }

    @Override
    public ClinicDto updateOut(Long id, ClinicRequest request) {
        Optional<Clinic> extractedData = clinicRepository.findById(id);
        if(extractedData.isPresent()){
            Clinic clinic = getEntity(extractedData.get(), request,true, id);
            return ClinicMapper.fromEntity(clinicRepository.save(clinic));
        }else {
            throw new RuntimeException();
        }
    }

    @Override
    public ClinicDto deleteOut(Long id) {
        Optional<Clinic> extractedData = clinicRepository.findById(id);
        if(extractedData.isPresent()){
            extractedData.get().setStatus(0);
            extractedData.get().setDeletedBy(Constants.USU_ADMIN);
            extractedData.get().setDeletedOn(getTimestamp());
            return ClinicMapper.fromEntity(clinicRepository.save(extractedData.get()));
        }else {
            throw new RuntimeException();
        }
    }

    // Support Methods
    private Clinic getEntity(Clinic entity, ClinicRequest clinicRequest, boolean updateIf, Long id){
        SunatDto SunatDto = getExecSunat(clinicRequest.getIdentificationNumber());

        entity.setIdentificationType(SunatDto.getTipoDocumento());
        entity.setIdentificationNumber(SunatDto.getNumeroDocumento());
        entity.setName(SunatDto.getRazonSocial());
        entity.setAddress(SunatDto.getDireccion());
        entity.setPhone(clinicRequest.getPhone());
        entity.setEmail(clinicRequest.getEmail());
        entity.setWebsite(clinicRequest.getWebsite());
        entity.setStatus(Constants.STATUS_ACTIVE);

        if (updateIf) {
            entity.setId(id);
            entity.setUpdatedBy(Constants.USU_ADMIN);
            entity.setUpdatedOn(getTimestamp());
        } else {
            entity.setCreatedBy(Constants.USU_ADMIN);
            entity.setCreatedOn(getTimestamp());
        }
        return entity;
    }

    private SunatDto getExecSunat(String identificationNumber){
        String authorization = "Bearer " + tokenSunat;
        return clientSunat.getInfoSunat(identificationNumber, authorization);
    }

    private Timestamp getTimestamp(){
        long currenTime = System.currentTimeMillis();
        return new Timestamp(currenTime);
    }
}
