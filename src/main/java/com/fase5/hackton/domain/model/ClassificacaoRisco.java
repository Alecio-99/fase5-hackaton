package com.fase5.hackton.domain.model;

public enum ClassificacaoRisco {
	VERMELHO(1),
	LARANJA(2),
	AMARELO(3),
	VERDE(4),
	AZUL(5);

	private final int prioridade;

	ClassificacaoRisco(int prioridade) {
		this.prioridade = prioridade;
	}

	public int prioridade() {
		return prioridade;
	}
}
