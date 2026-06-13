package com.fase5.hackton.domain.model;

import java.time.OffsetDateTime;

public record HistoricoListaEspera(
		Long id,
		Long pacienteId,
		Long consultaId,
		String especialidade,
		String descricao,
		OffsetDateTime dataMovimentacao
) {
}
