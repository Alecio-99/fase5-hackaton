package com.fase5.hackton.domain.model;

import java.time.OffsetDateTime;

public record FilaAtendimento(
		Long id,
		Long pacienteId,
		Long unidadeSaudeId,
		ClassificacaoRisco classificacaoRisco,
		OffsetDateTime horarioEntrada,
		StatusFila status
) {
	public FilaAtendimento comStatus(StatusFila novoStatus) {
		return new FilaAtendimento(id, pacienteId, unidadeSaudeId, classificacaoRisco, horarioEntrada, novoStatus);
	}
}
