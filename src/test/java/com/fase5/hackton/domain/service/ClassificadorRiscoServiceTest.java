package com.fase5.hackton.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.fase5.hackton.domain.model.ClassificacaoRisco;
import com.fase5.hackton.domain.model.RespostasTriagem;
import org.junit.jupiter.api.Test;

class ClassificadorRiscoServiceTest {
	private final ClassificadorRiscoService service = new ClassificadorRiscoService();

	@Test
	void deveClassificarVermelhoQuandoHaFaltaDeAr() {
		RespostasTriagem respostas = new RespostasTriagem(false, true, false, false, false);

		assertThat(service.classificar(respostas)).isEqualTo(ClassificacaoRisco.VERMELHO);
	}

	@Test
	void deveClassificarLaranjaQuandoHaDorIntensaSemCriterioVermelho() {
		RespostasTriagem respostas = new RespostasTriagem(false, false, true, false, false);

		assertThat(service.classificar(respostas)).isEqualTo(ClassificacaoRisco.LARANJA);
	}

	@Test
	void deveClassificarAzulQuandoNaoHaSintomasRelevantes() {
		RespostasTriagem respostas = new RespostasTriagem(false, false, false, false, false);

		assertThat(service.classificar(respostas)).isEqualTo(ClassificacaoRisco.AZUL);
	}
}
