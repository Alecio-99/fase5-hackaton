package com.fase5.hackton.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fase5.hackton.application.port.FilaRepositoryPort;
import com.fase5.hackton.application.port.PacienteRepositoryPort;
import com.fase5.hackton.application.port.TriagemRepositoryPort;
import com.fase5.hackton.application.port.UnidadeSaudeRepositoryPort;
import com.fase5.hackton.domain.exception.RecursoNaoEncontradoException;
import com.fase5.hackton.domain.exception.RegraNegocioException;
import com.fase5.hackton.domain.model.ClassificacaoRisco;
import com.fase5.hackton.domain.model.FilaAtendimento;
import com.fase5.hackton.domain.model.Paciente;
import com.fase5.hackton.domain.model.RespostasTriagem;
import com.fase5.hackton.domain.model.StatusFila;
import com.fase5.hackton.domain.model.Triagem;
import com.fase5.hackton.domain.model.TipoUnidade;
import com.fase5.hackton.domain.model.UnidadeSaude;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FilaServiceTest {

	@Mock
	private FilaRepositoryPort filaRepository;
	@Mock
	private PacienteRepositoryPort pacienteRepository;
	@Mock
	private UnidadeSaudeRepositoryPort unidadeRepository;
	@Mock
	private TriagemRepositoryPort triagemRepository;

	@InjectMocks
	private FilaService filaService;

	private Paciente paciente() {
		return new Paciente(1L, "Joao", "12345678901", LocalDate.of(1990, 1, 1), "1199", "j@x.com", "Rua A", "700", OffsetDateTime.now());
	}

	private UnidadeSaude unidade() {
		return new UnidadeSaude(2L, "UBS Centro", TipoUnidade.UBS, "Rua B", "1133", 50);
	}

	private Triagem triagem(Long pacienteId, ClassificacaoRisco risco) {
		return new Triagem(3L, pacienteId, OffsetDateTime.now(),
				new RespostasTriagem(true, false, false, false, false), risco, null);
	}

	@Test
	void deveEntrarNaFilaComStatusAguardandoEClassificacaoDaTriagem() {
		when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente()));
		when(unidadeRepository.findById(2L)).thenReturn(Optional.of(unidade()));
		when(triagemRepository.findById(3L)).thenReturn(Optional.of(triagem(1L, ClassificacaoRisco.AMARELO)));
		when(filaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		FilaAtendimento resultado = filaService.entrarNaFila(1L, 2L, 3L);

		assertThat(resultado.status()).isEqualTo(StatusFila.AGUARDANDO);
		assertThat(resultado.classificacaoRisco()).isEqualTo(ClassificacaoRisco.AMARELO);
		assertThat(resultado.pacienteId()).isEqualTo(1L);
		assertThat(resultado.unidadeSaudeId()).isEqualTo(2L);
	}

	@Test
	void deveFalharQuandoPacienteNaoExiste() {
		when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> filaService.entrarNaFila(1L, 2L, 3L))
				.isInstanceOf(RecursoNaoEncontradoException.class);
		verify(filaRepository, never()).save(any());
	}

	@Test
	void deveFalharQuandoTriagemNaoPertenceAoPaciente() {
		when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente()));
		when(unidadeRepository.findById(2L)).thenReturn(Optional.of(unidade()));
		when(triagemRepository.findById(3L)).thenReturn(Optional.of(triagem(99L, ClassificacaoRisco.VERDE)));

		assertThatThrownBy(() -> filaService.entrarNaFila(1L, 2L, 3L))
				.isInstanceOf(RegraNegocioException.class)
				.hasMessageContaining("nao pertence");
	}

	@Test
	void consultarPosicaoDeveSomarUmAoTotalDeQuemEstaNaFrente() {
		FilaAtendimento fila = new FilaAtendimento(5L, 1L, 2L, ClassificacaoRisco.VERDE, OffsetDateTime.now(), StatusFila.AGUARDANDO);
		when(filaRepository.findById(5L)).thenReturn(Optional.of(fila));
		when(filaRepository.countBefore(2L, StatusFila.AGUARDANDO, ClassificacaoRisco.VERDE, fila.horarioEntrada()))
				.thenReturn(3L);

		assertThat(filaService.consultarPosicao(5L)).isEqualTo(4);
	}

	@Test
	void consultarPosicaoDeveRetornarZeroQuandoNaoEstaAguardando() {
		FilaAtendimento fila = new FilaAtendimento(5L, 1L, 2L, ClassificacaoRisco.VERDE, OffsetDateTime.now(), StatusFila.EM_ATENDIMENTO);
		when(filaRepository.findById(5L)).thenReturn(Optional.of(fila));

		assertThat(filaService.consultarPosicao(5L)).isZero();
	}

	@Test
	void chamarProximoDevePriorizarMaiorGravidadeDepoisChegada() {
		OffsetDateTime agora = OffsetDateTime.now();
		FilaAtendimento azulCedo = new FilaAtendimento(10L, 1L, 2L, ClassificacaoRisco.AZUL, agora.minusMinutes(30), StatusFila.AGUARDANDO);
		FilaAtendimento vermelhoTarde = new FilaAtendimento(11L, 4L, 2L, ClassificacaoRisco.VERMELHO, agora, StatusFila.AGUARDANDO);
		when(filaRepository.findByUnidadeAndStatus(2L, StatusFila.AGUARDANDO))
				.thenReturn(List.of(azulCedo, vermelhoTarde));
		when(filaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		FilaAtendimento chamado = filaService.chamarProximo(2L);

		assertThat(chamado.id()).isEqualTo(11L);
		assertThat(chamado.status()).isEqualTo(StatusFila.EM_ATENDIMENTO);
	}

	@Test
	void chamarProximoDeveFalharQuandoNaoHaPacientesAguardando() {
		when(filaRepository.findByUnidadeAndStatus(2L, StatusFila.AGUARDANDO)).thenReturn(List.of());

		assertThatThrownBy(() -> filaService.chamarProximo(2L))
				.isInstanceOf(RecursoNaoEncontradoException.class);
	}

	@Test
	void finalizarDeveAlterarStatusParaFinalizado() {
		FilaAtendimento fila = new FilaAtendimento(7L, 1L, 2L, ClassificacaoRisco.VERDE, OffsetDateTime.now(), StatusFila.EM_ATENDIMENTO);
		when(filaRepository.findById(7L)).thenReturn(Optional.of(fila));
		when(filaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		ArgumentCaptor<FilaAtendimento> captor = ArgumentCaptor.forClass(FilaAtendimento.class);
		filaService.finalizar(7L);

		verify(filaRepository).save(captor.capture());
		assertThat(captor.getValue().status()).isEqualTo(StatusFila.FINALIZADO);
	}

	@Test
	void cancelarDeveAlterarStatusParaCancelado() {
		FilaAtendimento fila = new FilaAtendimento(8L, 1L, 2L, ClassificacaoRisco.VERDE, OffsetDateTime.now(), StatusFila.AGUARDANDO);
		when(filaRepository.findById(8L)).thenReturn(Optional.of(fila));
		when(filaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		assertThat(filaService.cancelar(8L).status()).isEqualTo(StatusFila.CANCELADO);
	}

	@Test
	void buscarFilaInexistenteDeveFalhar() {
		when(filaRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> filaService.finalizar(404L))
				.isInstanceOf(RecursoNaoEncontradoException.class);
	}
}
