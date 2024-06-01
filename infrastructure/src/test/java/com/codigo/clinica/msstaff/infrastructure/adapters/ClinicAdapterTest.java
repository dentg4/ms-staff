package com.codigo.clinica.msstaff.infrastructure.adapters;

import com.codigo.clinica.msstaff.domain.aggregates.constants.Constants;
import com.codigo.clinica.msstaff.domain.aggregates.dto.ClinicDto;
import com.codigo.clinica.msstaff.domain.aggregates.dto.SunatDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.ClinicRequest;
import com.codigo.clinica.msstaff.infrastructure.client.ClientSunat;
import com.codigo.clinica.msstaff.infrastructure.dao.ClinicRepository;
import com.codigo.clinica.msstaff.infrastructure.entity.Clinic;
import com.codigo.clinica.msstaff.infrastructure.mapper.ClinicMapper;
import com.codigo.clinica.msstaff.infrastructure.redis.RedisService;
import com.codigo.clinica.msstaff.infrastructure.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class ClinicAdapterTest {

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = openMocks(this);
    }

    @Mock
    private ClinicRepository clinicRepository;

    @Mock
    private ClientSunat clientSunat;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private ClinicAdapter clinicAdapter;

    @Test
    void createOut() {
        //ARRANGE
        Clinic clinic = new Clinic();
        clinic.setIdentificationNumber("41036987");
        clinic.setPhone("998877665");
        clinic.setEmail("hola@example.com");
        clinic.setWebsite("www.google.com");
        clinic.setStatus(Constants.STATUS_ACTIVE);
        clinic.setCreatedBy(Constants.USU_ADMIN);
        clinic.setCreatedOn(new Timestamp(System.currentTimeMillis()));

        ClinicRequest request = ClinicRequest.builder()
                .identificationNumber("41036987")
                .build();

        //SIMULATE
        when(clinicRepository.save(any(Clinic.class))).thenReturn(clinic);
        when(clientSunat.getInfoSunat(anyString(), anyString())).thenReturn(new SunatDto());

        //ACT
        ClinicDto clinicDto = clinicAdapter.createOut(request);

        //ASSERT
        assertNotNull(clinicDto);
        assertEquals(clinic.getIdentificationNumber(), clinicDto.getIdentificationNumber());
        assertEquals(clinic.getPhone(), clinicDto.getPhone());
        assertEquals(clinic.getEmail(), clinicDto.getEmail());
        assertEquals(clinic.getWebsite(), clinicDto.getWebsite());
        assertNotNull(clinicDto.getStatus());
        assertNotNull(clinicDto.getCreatedBy());
        assertNotNull(clinicDto.getCreateOn());
        verify(clinicRepository).save(any(Clinic.class));
    }

    @Test
    void findByIdOutForRedis() {
        //ARRANGE
        Long id= 1L;
        Clinic clinic = new Clinic();
        clinic.setIdentificationNumber("41036987");
        clinic.setPhone("998877665");
        clinic.setEmail("hola@example.com");
        clinic.setWebsite("www.google.com");
        ClinicDto clinicDto = ClinicMapper.fromEntity(clinic);

        //SIMULATE
        when(redisService.getFromRedis(anyString())).thenReturn(Util.convertToString(clinicDto));

        //ACT
        Optional<ClinicDto> response = clinicAdapter.findByIdOut(id);

        //ASSERT
        assertNotNull(response);
        assertEquals(clinicDto.getIdentificationNumber(), response.get().getIdentificationNumber());
        assertEquals(clinicDto.getPhone(), response.get().getPhone());
        assertEquals(clinicDto.getEmail(), response.get().getEmail());
        assertEquals(clinicDto.getWebsite(), response.get().getWebsite());
    }

    @Test
    void findByIdOutForBD() {
        //ARRANGE
        Long id= 1L;
        Clinic clinic = new Clinic();
        clinic.setIdentificationNumber("41036987");
        clinic.setEmail("hola@example.com");

        //SIMULATE
        when(redisService.getFromRedis(anyString())).thenReturn(null);
        when(clinicRepository.findById(anyLong())).thenReturn(Optional.of(clinic));

        //ACT
        Optional<ClinicDto> response = clinicAdapter.findByIdOut(id);

        //ASSERT
        assertNotNull(response);
    }

    @Test
    void findByIdOutNotFound() {
        //ARRANGE
        Long id = 1L;

        //SIMULATE
        when(redisService.getFromRedis(anyString())).thenReturn(null);
        when(clinicRepository.findById(anyLong())).thenReturn(Optional.empty());

        //ASSERT
        assertThrows(RuntimeException.class, () -> clinicAdapter.findByIdOut(id));
        verify(redisService).getFromRedis(anyString());
        verify(clinicRepository).findById(anyLong());
    }

    @Test
    void getAllOutNoList(){
        //SIMULATE
        when(clinicRepository.findAll()).thenReturn(Collections.emptyList());

        //ACT
        List<ClinicDto> response = clinicAdapter.getAllOut();

        //ASSERT
        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(clinicRepository).findAll();
    }

    @Test
    void getAllOut() {
        //ARRANGE
        Clinic clinic = new Clinic();
        clinic.setIdentificationNumber("41036987");
        clinic.setIdentificationNumber("hola@example.com");

        Clinic newClinic = new Clinic();
        newClinic.setIdentificationNumber("11223344");
        newClinic.setIdentificationNumber("hello@example.com");

        List<Clinic> clinicList = new ArrayList<>();
        clinicList.add(clinic);
        clinicList.add(newClinic);

        //SIMULATE
        when(clinicRepository.findAll()).thenReturn(clinicList);

        //ACT
        List<ClinicDto> clinicListDto = clinicList.stream().map(ClinicMapper::fromEntity).toList();
        List<ClinicDto> response = clinicAdapter.getAllOut();

        //ASSERT
        assertNotNull(response);
        assertEquals(clinicListDto.size(), response.size());
    }

    @Test
    void updateOut() {
        //ARRANGE
        Long id = 1L;
        Clinic clinic = new Clinic();
        clinic.setIdentificationNumber("41036987");
        clinic.setIdentificationNumber("hola@example.com");

        ClinicRequest request = ClinicRequest.builder()
                .identificationNumber("11223344")
                .build();

        SunatDto sunatDto = SunatDto.builder()
                .numeroDocumento(request.getIdentificationNumber())
                .razonSocial("New name")
                .build();

        //SIMULATE
        when(clinicRepository.findById(anyLong())).thenReturn(Optional.of(clinic));
        //when(patientRepository.findById(anyLong())).thenReturn(Optional.of(new Patient()));
        when(clientSunat.getInfoSunat(anyString(), anyString())).thenReturn(sunatDto);
        when(clinicRepository.save(any(Clinic.class))).thenReturn(clinic);

        //ACT
        ClinicDto clinicDto = clinicAdapter.updateOut(id, request);

        //ASSERT
        assertNotNull(clinicDto);
        assertEquals(request.getIdentificationNumber(), clinicDto.getIdentificationNumber());
    }

    @Test
    void deleteOut() {
        //ARRANGE
        Long id = 1L;
        Clinic clinic = new Clinic();
        clinic.setId(id);
        clinic.setIdentificationNumber("41036987");
        clinic.setEmail("hola@example.com");
        clinic.setStatus(Constants.STATUS_INACTIVE);

        //SIMULATE
        when(clinicRepository.findById(anyLong())).thenReturn(Optional.of(clinic));
        when(clinicRepository.save(any(Clinic.class))).thenReturn(clinic);

        //ACT
        ClinicDto clinicDto= clinicAdapter.deleteOut(id);

        //ASSERT
        assertNotNull(clinicDto);
        assertEquals(id, clinicDto.getId());
        assertEquals(clinic.getIdentificationNumber(), clinicDto.getIdentificationNumber());
        assertEquals(Constants.STATUS_INACTIVE, clinicDto.getStatus());
    }
}