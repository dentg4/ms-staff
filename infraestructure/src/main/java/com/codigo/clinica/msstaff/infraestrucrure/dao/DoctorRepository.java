package com.codigo.clinica.msstaff.infraestrucrure.dao;

import com.codigo.clinica.msstaff.infraestrucrure.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
