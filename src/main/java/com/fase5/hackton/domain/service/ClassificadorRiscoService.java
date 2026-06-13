package com.fase5.hackton.domain.service;

import com.fase5.hackton.domain.model.ClassificacaoRisco;
import com.fase5.hackton.domain.model.RespostasTriagem;

public class ClassificadorRiscoService {
	public ClassificacaoRisco classificar(RespostasTriagem respostas) {
		if (respostas.possuiFaltaDeAr() || respostas.possuiSangramento()) {
			return ClassificacaoRisco.VERMELHO;
		}
		if (respostas.possuiDorIntensa()) {
			return ClassificacaoRisco.LARANJA;
		}
		if (respostas.possuiFebre() && respostas.possuiTontura()) {
			return ClassificacaoRisco.AMARELO;
		}
		if (respostas.possuiFebre() || respostas.possuiTontura()) {
			return ClassificacaoRisco.VERDE;
		}
		return ClassificacaoRisco.AZUL;
	}
}
