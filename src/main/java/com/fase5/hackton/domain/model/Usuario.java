package com.fase5.hackton.domain.model;

public record Usuario(
		Long id,
		String nome,
		String email,
		String senhaHash,
		Perfil perfil
) {
}
