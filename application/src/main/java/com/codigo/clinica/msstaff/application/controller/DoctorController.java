package com.codigo.clinica.msstaff.application.controller;

import com.codigo.clinica.msstaff.domain.aggregates.dto.DoctorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorController {

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctoById(@PathVariable Long id) {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setId(id);
        doctorDto.setName("Doctor " + id);
        doctorDto.setSurname("Surname doctor");
        return ResponseEntity.ok(doctorDto);
    }
}
