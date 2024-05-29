package com.codigo.clinica.msstaff.infrastructure.dao;

import com.codigo.clinica.msstaff.infrastructure.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
