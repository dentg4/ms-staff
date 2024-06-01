package com.codigo.clinica.msstaff.infrastructure.adapters;

import com.codigo.clinica.msstaff.domain.aggregates.constants.Constants;
import com.codigo.clinica.msstaff.domain.aggregates.dto.ReniecDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.DoctorRequest;
import com.codigo.clinica.msstaff.domain.aggregates.dto.DoctorDto;
import com.codigo.clinica.msstaff.infrastructure.client.ClientReniec;
import com.codigo.clinica.msstaff.infrastructure.dao.ClinicRepository;
import com.codigo.clinica.msstaff.infrastructure.dao.DoctorRepository;
import com.codigo.clinica.msstaff.infrastructure.entity.Clinic;
import com.codigo.clinica.msstaff.infrastructure.entity.Doctor;
import com.codigo.clinica.msstaff.infrastructure.mapper.DoctorMapper;
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

class DoctorAdapterTest {

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = openMocks(this);
    }

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ClinicRepository clinicRepository;

    @Mock
    private ClientReniec clientReniec;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private DoctorAdapter doctorAdapter;

    @Test
    void createOut() {
        //ARRANGE
        Doctor doctor = new Doctor();
        doctor.setIdentificationNumber("46892014");
        doctor.setCmp("123456");
        doctor.setClinic(new Clinic());
        doctor.setStatus(Constants.STATUS_ACTIVE);
        doctor.setCreatedBy(Constants.USU_ADMIN);
        doctor.setCreatedOn(new Timestamp(System.currentTimeMillis()));

        DoctorRequest request = DoctorRequest.builder()
                .identificationNumber("46892014")
                .cmp("123456")
                .clinic("20305944801")
                .build();

        //SIMULATE
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        when(clinicRepository.findByIdentificationNumber(anyString())).thenReturn(Optional.of(new Clinic()));
        when(clientReniec.getInfoReniec(anyString(), anyString())).thenReturn(new ReniecDto());

        //ACT
        DoctorDto doctorDto = doctorAdapter.createOut(request);

        //ASSERT
        assertNotNull(doctorDto);
        assertEquals(doctor.getIdentificationNumber(), doctorDto.getIdentificationNumber());
        assertEquals(doctor.getCmp(), doctorDto.getCmp());
        assertNotNull(doctorDto.getStatus());
        assertNotNull(doctorDto.getCreatedBy());
        assertNotNull(doctorDto.getCreateOn());
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    void findByIdOutForRedis() {
        //ARRANGE
        Long id= 1L;
        Doctor doctor = new Doctor();
        doctor.setIdentificationNumber("46892014");
        doctor.setClinic(new Clinic());
        DoctorDto prescriptionDto = DoctorMapper.fromEntity(doctor);

        //SIMULATE
        when(redisService.getFromRedis(anyString())).thenReturn(Util.convertToString(prescriptionDto));

        //ACT
        Optional<DoctorDto> response = doctorAdapter.findByIdOut(id);

        //ASSERT
        assertNotNull(response);
        assertEquals(prescriptionDto.getIdentificationNumber(), response.get().getIdentificationNumber());
    }

    @Test
    void findByIdOutForBD() {
        //ARRANGE
        Long id= 1L;
        Doctor doctor = new Doctor();
        doctor.setIdentificationNumber("46892014");
        doctor.setClinic(new Clinic());

        //SIMULATE
        when(redisService.getFromRedis(anyString())).thenReturn(null);
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        //ACT
        Optional<DoctorDto> response = doctorAdapter.findByIdOut(id);

        //ASSERT
        assertNotNull(response);
    }

    @Test
    void findByIdOutNotFound() {
        //ARRANGE
        Long id = 1L;

        //SIMULATE
        when(redisService.getFromRedis(anyString())).thenReturn(null);
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        //ASSERT
        assertThrows(RuntimeException.class, () -> doctorAdapter.findByIdOut(id));
        verify(redisService).getFromRedis(anyString());
        verify(doctorRepository).findById(anyLong());
    }

    @Test
    void getAllOutNoList(){
        //SIMULATE
        when(doctorRepository.findAll()).thenReturn(Collections.emptyList());

        //ACT
        List<DoctorDto> response = doctorAdapter.getAllOut();

        //ASSERT
        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(doctorRepository).findAll();
    }

    @Test
    void getAllOut() {
        //ARRANGE
        Doctor doctor = new Doctor();
        doctor.setIdentificationNumber("46892014");
        doctor.setClinic(new Clinic());

        Doctor newDoctor = new Doctor();
        newDoctor.setIdentificationNumber("12345678");
        newDoctor.setClinic(new Clinic());

        List<Doctor> doctorList = new ArrayList<>();
        doctorList.add(doctor);
        doctorList.add(newDoctor);

        //SIMULATE
        when(doctorRepository.findAll()).thenReturn(doctorList);

        //ACT
        List<DoctorDto> doctorListDto = doctorList.stream().map(DoctorMapper::fromEntity).toList();
        List<DoctorDto> response = doctorAdapter.getAllOut();

        //ASSERT
        assertNotNull(response);
        assertEquals(doctorListDto.size(), response.size());
    }

    @Test
    void updateOut() {
        //ARRANGE
        Long id = 1L;

        Clinic clinic = new Clinic();
        clinic.setId(1L);
        clinic.setIdentificationNumber("20305944801");

        Doctor doctor = new Doctor();
        doctor.setIdentificationNumber("46892014");
        doctor.setClinic(clinic);

        DoctorRequest request = DoctorRequest.builder()
                .identificationNumber("12345678")
                .clinic(clinic.getIdentificationNumber())
                .build();

        ReniecDto reniecDto = ReniecDto.builder()
                .numeroDocumento(request.getIdentificationNumber())
                .nombres("New Name")
                .build();

        //SIMULATE
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(clinicRepository.findByIdentificationNumber(anyString())).thenReturn(Optional.of(clinic));
        when(clientReniec.getInfoReniec(anyString(), anyString())).thenReturn(reniecDto);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        //ACT
        DoctorDto doctorDto = doctorAdapter.updateOut(id, request);

        //ASSERT
        assertNotNull(doctorDto);
        assertEquals(request.getIdentificationNumber(), doctorDto.getIdentificationNumber());
    }

    @Test
    void deleteOut() {
        //ARRANGE
        Long id = 1L;
        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setIdentificationNumber("46892014");
        doctor.setClinic(new Clinic());
        doctor.setStatus(Constants.STATUS_INACTIVE);

        //SIMULATE
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        //ACT
        DoctorDto doctorDto= doctorAdapter.deleteOut(id);

        //ASSERT
        assertNotNull(doctorDto);
        assertEquals(id, doctorDto.getId());
        assertEquals(doctor.getIdentificationNumber(), doctorDto.getIdentificationNumber());
        assertEquals(Constants.STATUS_INACTIVE, doctorDto.getStatus());
    }
}