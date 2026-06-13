package com.fase5.hackton.domain.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record Paciente(
		Long id,
		String nome,
		String cpf,
		LocalDate dataNascimento,
		String telefone,
		String email,
		String endereco,
		String cartaoSus,
		OffsetDateTime dataCadastro
) {
}
