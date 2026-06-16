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

	@Test
	void deveClassificarVermelhoQuandoHaSangramento() {
		RespostasTriagem respostas = new RespostasTriagem(false, false, false, true, false);

		assertThat(service.classificar(respostas)).isEqualTo(ClassificacaoRisco.VERMELHO);
	}

	@Test
	void deveClassificarAmareloQuandoHaFebreETontura() {
		RespostasTriagem respostas = new RespostasTriagem(true, false, false, false, true);

		assertThat(service.classificar(respostas)).isEqualTo(ClassificacaoRisco.AMARELO);
	}

	@Test
	void deveClassificarVerdeQuandoHaApenasFebre() {
		RespostasTriagem respostas = new RespostasTriagem(true, false, false, false, false);

		assertThat(service.classificar(respostas)).isEqualTo(ClassificacaoRisco.VERDE);
	}

	@Test
	void deveClassificarVerdeQuandoHaApenasTontura() {
		RespostasTriagem respostas = new RespostasTriagem(false, false, false, false, true);

		assertThat(service.classificar(respostas)).isEqualTo(ClassificacaoRisco.VERDE);
	}

	@Test
	void criterioVermelhoDevePrevalecerSobreDorIntensa() {
		RespostasTriagem respostas = new RespostasTriagem(false, true, true, false, false);

		assertThat(service.classificar(respostas)).isEqualTo(ClassificacaoRisco.VERMELHO);
	}
}
