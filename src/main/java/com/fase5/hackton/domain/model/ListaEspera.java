package com.fase5.hackton.domain.model;

import java.time.OffsetDateTime;

public record ListaEspera(
		Long id,
		Long pacienteId,
		String especialidade,
		Integer prioridade,
		OffsetDateTime dataCadastro
) {
}
