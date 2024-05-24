package com.codigo.clinica.msstaff.infraestrucrure.dao;

import com.codigo.clinica.msstaff.infraestrucrure.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
}
