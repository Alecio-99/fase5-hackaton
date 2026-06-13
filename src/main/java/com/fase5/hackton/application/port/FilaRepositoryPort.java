package com.fase5.hackton.application.port;

import com.fase5.hackton.domain.model.ClassificacaoRisco;
import com.fase5.hackton.domain.model.FilaAtendimento;
import com.fase5.hackton.domain.model.StatusFila;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface FilaRepositoryPort {
	FilaAtendimento save(FilaAtendimento filaAtendimento);
	Optional<FilaAtendimento> findById(Long id);
	List<FilaAtendimento> findByUnidadeAndStatus(Long unidadeSaudeId, StatusFila status);
	long countBefore(Long unidadeSaudeId, StatusFila status, ClassificacaoRisco risco, OffsetDateTime horarioEntrada);
	long countByStatus(StatusFila status);
}
