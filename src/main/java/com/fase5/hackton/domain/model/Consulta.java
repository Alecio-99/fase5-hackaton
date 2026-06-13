package com.fase5.hackton.domain.model;

import java.time.OffsetDateTime;

public record Consulta(
		Long id,
		Long pacienteId,
		Long unidadeSaudeId,
		OffsetDateTime dataHoraConsulta,
		String especialidade,
		StatusConsulta status
) {
	public Consulta comStatus(StatusConsulta novoStatus) {
		return new Consulta(id, pacienteId, unidadeSaudeId, dataHoraConsulta, especialidade, novoStatus);
	}
}
