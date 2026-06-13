package com.fase5.hackton.infrastructure.persistence.repository;

import com.fase5.hackton.infrastructure.persistence.entity.ListaEsperaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringListaEsperaRepository extends JpaRepository<ListaEsperaEntity, Long> {
	Optional<ListaEsperaEntity> findFirstByEspecialidadeOrderByPrioridadeAscDataCadastroAsc(String especialidade);
}
