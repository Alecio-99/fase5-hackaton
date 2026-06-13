package com.fase5.hackton.infrastructure.persistence.repository;

import com.fase5.hackton.domain.model.ClassificacaoRisco;
import com.fase5.hackton.domain.model.StatusFila;
import com.fase5.hackton.infrastructure.persistence.entity.FilaAtendimentoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpringFilaRepository extends JpaRepository<FilaAtendimentoEntity, Long> {
	List<FilaAtendimentoEntity> findByUnidadeSaudeIdAndStatus(Long unidadeSaudeId, StatusFila status);
	long countByStatus(StatusFila status);

	@Query("select f.classificacaoRisco, count(f) from FilaAtendimentoEntity f group by f.classificacaoRisco")
	List<Object[]> countAtendimentosPorClassificacao();
}
