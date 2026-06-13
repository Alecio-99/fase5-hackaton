package com.fase5.hackton.domain.model;

public record UnidadeSaude(
		Long id,
		String nome,
		TipoUnidade tipo,
		String endereco,
		String telefone,
		Integer capacidadeAtendimento
) {
}
