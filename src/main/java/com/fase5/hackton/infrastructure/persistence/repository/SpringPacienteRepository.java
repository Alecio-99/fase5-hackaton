package com.fase5.hackton.infrastructure.persistence.repository;

import com.fase5.hackton.infrastructure.persistence.entity.PacienteEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringPacienteRepository extends JpaRepository<PacienteEntity, Long> {
	Optional<PacienteEntity> findByCpf(String cpf);
	boolean existsByCpf(String cpf);
	boolean existsByCartaoSus(String cartaoSus);
}
