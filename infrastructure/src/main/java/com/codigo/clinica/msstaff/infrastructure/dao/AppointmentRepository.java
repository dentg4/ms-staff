package com.codigo.clinica.msstaff.infrastructure.dao;

import com.codigo.clinica.msstaff.infrastructure.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
