package com.fase5.hackton.infrastructure.persistence.repository;

import com.fase5.hackton.domain.model.StatusConsulta;
import com.fase5.hackton.infrastructure.persistence.entity.ConsultaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringConsultaRepository extends JpaRepository<ConsultaEntity, Long> {
	long countByStatus(StatusConsulta status);
}
