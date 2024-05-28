package com.codigo.clinica.msstaff.infrastructure.dao;

import com.codigo.clinica.msstaff.infrastructure.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    Clinic findByIdentificationNumber(String identificationNumber);
}
