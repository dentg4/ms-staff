package com.codigo.clinica.msstaff.infrastructure.adapters;

import com.codigo.clinica.msstaff.domain.aggregates.constants.Constants;
import com.codigo.clinica.msstaff.domain.aggregates.dto.AppointmentDto;
import com.codigo.clinica.msstaff.domain.aggregates.dto.PatientDto;
import com.codigo.clinica.msstaff.domain.aggregates.request.AppointmentRequest;
import com.codigo.clinica.msstaff.infrastructure.dao.AppointmentRepository;
import com.codigo.clinica.msstaff.infrastructure.dao.DoctorRepository;
import com.codigo.clinica.msstaff.infrastructure.client.ClientMsPatient;
import com.codigo.clinica.msstaff.infrastructure.entity.Appointment;
import com.codigo.clinica.msstaff.infrastructure.entity.Doctor;
import com.codigo.clinica.msstaff.infrastructure.mapper.AppointmentMapper;
import com.codigo.clinica.msstaff.infrastructure.redis.RedisService;
import com.codigo.clinica.msstaff.infrastructure.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AppointmentAdapterTest {

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = openMocks(this);
    }

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ClientMsPatient clientMsPatient;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private AppointmentAdapter appointmentAdapter;

    @Test
    void createOut() {
        //ARRANGE
        Appointment appointment = new Appointment();
        appointment.setDate(new Timestamp(System.currentTimeMillis()));
        appointment.setPatient(1L);
        appointment.setDoctor(new Doctor());
        appointment.setStatus(Constants.STATUS_ACTIVE);
        appointment.setCreatedBy(Constants.USU_ADMIN);
        appointment.setCreatedOn(new Timestamp(System.currentTimeMillis()));

        AppointmentRequest request = AppointmentRequest.builder()
                .date(new Timestamp(System.currentTimeMillis()))
                .patient(1L)
                .doctor(1L)
                .build();

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(new Doctor()));
        when(clientMsPatient.getPatientById(anyLong())).thenReturn(ResponseEntity.ok(new PatientDto()));

        //ACT
        AppointmentDto appointmentDto = appointmentAdapter.createOut(request);

        //ASSERT
        assertNotNull(appointmentDto);
        assertEquals(appointment.getDate(), appointmentDto.getDate());
        assertEquals(appointment.getDuration(), appointmentDto.getDuration());
        assertNotNull(appointmentDto.getStatus());
        assertNotNull(appointmentDto.getCreatedBy());
        assertNotNull(appointmentDto.getCreateOn());
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void findByIdOutForRedis() {
        //ARRANGE
        Long id= 1L;
        Appointment appointment = new Appointment();
        appointment.setDate(new Timestamp(System.currentTimeMillis()));
        appointment.setPatient(1L);
        appointment.setDoctor(new Doctor());

        AppointmentDto appointmentDto = AppointmentMapper.fromEntity(appointment);

        //SIMULATE
        when(redisService.getFromRedis(anyString())).thenReturn(Util.convertToString(appointmentDto));

        //ACT
        Optional<AppointmentDto> response = appointmentAdapter.findByIdOut(id);

        //ASSERT
        assertNotNull(response);
        assertEquals(appointmentDto.getDate(),
                response.orElseThrow(() -> new RuntimeException("Record not found")).getDate());
    }

    @Test
    void findByIdOutForBD() {
        //ARRANGE
        Long id = 1L;
        Appointment appointment = new Appointment();
        appointment.setDate(new Timestamp(System.currentTimeMillis()));
        appointment.setPatient(1L);
        appointment.setDoctor(new Doctor());

        PatientDto patientDto = PatientDto.builder().idPatient(appointment.getPatient()).build();

        //SIMULATE
        when(redisService.getFromRedis(anyString())).thenReturn(null);
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
        when(clientMsPatient.getPatientById(anyLong())).thenReturn(ResponseEntity.ok(patientDto));

        //ACT
        Optional<AppointmentDto> response = appointmentAdapter.findByIdOut(id);

        //ASSERT
        assertNotNull(response);
    }

    @Test
    void findByIdOutNotFound() {
        //ARRANGE
        Long id = 1L;

        //SIMULATE
        when(redisService.getFromRedis(anyString())).thenReturn(null);
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        //ASSERT
        assertThrows(RuntimeException.class, () -> appointmentAdapter.findByIdOut(id));
        verify(redisService).getFromRedis(anyString());
        verify(appointmentRepository).findById(anyLong());
    }

    @Test
    void getAllOutNoList(){
        //SIMULATE
        when(appointmentRepository.findAll()).thenReturn(Collections.emptyList());

        //ACT
        List<AppointmentDto> response = appointmentAdapter.getAllOut();

        //ASSERT
        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(appointmentRepository).findAll();
    }

    @Test
    void getAllOut() {
        //ARRANGE
        Appointment appointment = new Appointment();
        appointment.setDate(new Timestamp(System.currentTimeMillis()));
        appointment.setPatient(1L);
        appointment.setDoctor(new Doctor());

        Appointment newAppointment = new Appointment();
        newAppointment.setDate(new Timestamp(System.currentTimeMillis()));
        newAppointment.setPatient(1L);
        newAppointment.setDoctor(new Doctor());

        List<Appointment> appointmentList = new ArrayList<>();
        appointmentList.add(appointment);
        appointmentList.add(newAppointment);

        //SIMULATE
        when(appointmentRepository.findAll()).thenReturn(appointmentList);

        //ACT
        List<AppointmentDto> appointmentListDto = appointmentList.stream().map(AppointmentMapper::fromEntity).toList();
        List<AppointmentDto> response = appointmentAdapter.getAllOut();

        //ASSERT
        assertNotNull(response);
        assertEquals(appointmentListDto.size(),response.size());
    }

    @Test
    void updateOut() {
        //ARRANGE
        Long id = 1L;
        Appointment appointment = new Appointment();
        appointment.setDate(new Timestamp(System.currentTimeMillis()));
        appointment.setPatient(1L);
        appointment.setDoctor(new Doctor());

        AppointmentRequest request = AppointmentRequest.builder()
                .date(new Timestamp(System.currentTimeMillis()))
                .patient(1L)
                .doctor(1L)
                .build();

        PatientDto patientDto = PatientDto.builder().idPatient(appointment.getPatient()).build();

        //SIMULATE
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(new Doctor()));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(clientMsPatient.getPatientById(anyLong())).thenReturn(ResponseEntity.ok(patientDto));

        //ACT
        AppointmentDto appointmentDto = appointmentAdapter.updateOut(id, request);

        //ASSERT
        assertNotNull(appointmentDto);
        assertEquals(request.getDate(), appointmentDto.getDate());
    }

    @Test
    void deleteOut() {
        //ARRANGE
        Long id = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setDate(new Timestamp(System.currentTimeMillis()));
        appointment.setPatient(1L);
        appointment.setDoctor(new Doctor());
        appointment.setStatus(Constants.STATUS_INACTIVE);

        //SIMULATE
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        //ACT
        AppointmentDto appointmentDto = appointmentAdapter.deleteOut(id);

        //ASSERT
        assertNotNull(appointmentDto);
        assertEquals(id, appointmentDto.getId());
        assertEquals(appointment.getDate(), appointmentDto.getDate());
        assertEquals(Constants.STATUS_INACTIVE, appointmentDto.getStatus());
    }
}