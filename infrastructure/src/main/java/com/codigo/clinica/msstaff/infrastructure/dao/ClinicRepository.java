package com.codigo.clinica.msstaff.infrastructure.dao;

import com.codigo.clinica.msstaff.infrastructure.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    Optional<Clinic> findByIdentificationNumber(String identificationNumber);
}
