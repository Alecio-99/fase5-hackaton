package com.fase5.hackton.domain.model;

import java.time.OffsetDateTime;

public record Triagem(
		Long id,
		Long pacienteId,
		OffsetDateTime dataTriagem,
		RespostasTriagem respostas,
		ClassificacaoRisco classificacaoRisco,
		String observacoes
) {
}
