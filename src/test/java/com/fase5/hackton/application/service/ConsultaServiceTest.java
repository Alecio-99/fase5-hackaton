package com.fase5.hackton.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fase5.hackton.application.port.ConsultaRepositoryPort;
import com.fase5.hackton.application.port.HistoricoListaEsperaRepositoryPort;
import com.fase5.hackton.application.port.ListaEsperaRepositoryPort;
import com.fase5.hackton.application.port.PacienteRepositoryPort;
import com.fase5.hackton.application.port.UnidadeSaudeRepositoryPort;
import com.fase5.hackton.domain.exception.RecursoNaoEncontradoException;
import com.fase5.hackton.domain.model.Consulta;
import com.fase5.hackton.domain.model.HistoricoListaEspera;
import com.fase5.hackton.domain.model.ListaEspera;
import com.fase5.hackton.domain.model.Paciente;
import com.fase5.hackton.domain.model.StatusConsulta;
import com.fase5.hackton.domain.model.TipoUnidade;
import com.fase5.hackton.domain.model.UnidadeSaude;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceTest {

	@Mock
	private ConsultaRepositoryPort consultaRepository;
	@Mock
	private PacienteRepositoryPort pacienteRepository;
	@Mock
	private UnidadeSaudeRepositoryPort unidadeRepository;
	@Mock
	private ListaEsperaRepositoryPort listaEsperaRepository;
	@Mock
	private HistoricoListaEsperaRepositoryPort historicoRepository;

	@InjectMocks
	private ConsultaService consultaService;

	private Consulta consulta(StatusConsulta status) {
		return new Consulta(1L, 5L, 2L, OffsetDateTime.now().plusDays(1), "CARDIOLOGIA", status);
	}

	@Test
	void agendarDeveValidarReferenciasESalvarComoAgendada() {
		when(pacienteRepository.findById(5L)).thenReturn(Optional.of(
				new Paciente(5L, "Ana", "1", LocalDate.of(1980, 5, 5), "1", "a@a.com", "Rua", "1", OffsetDateTime.now())));
		when(unidadeRepository.findById(2L)).thenReturn(Optional.of(
				new UnidadeSaude(2L, "UPA", TipoUnidade.UPA, "Rua", "2", 10)));
		when(consultaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		Consulta nova = new Consulta(null, 5L, 2L, OffsetDateTime.now().plusDays(2), "CARDIOLOGIA", null);
		Consulta resultado = consultaService.agendar(nova);

		assertThat(resultado.status()).isEqualTo(StatusConsulta.AGENDADA);
	}

	@Test
	void agendarDeveFalharQuandoUnidadeNaoExiste() {
		when(pacienteRepository.findById(5L)).thenReturn(Optional.of(
				new Paciente(5L, "Ana", "1", LocalDate.of(1980, 5, 5), "1", "a@a.com", "Rua", "1", OffsetDateTime.now())));
		when(unidadeRepository.findById(2L)).thenReturn(Optional.empty());

		Consulta nova = new Consulta(null, 5L, 2L, OffsetDateTime.now().plusDays(2), "CARDIOLOGIA", null);
		assertThatThrownBy(() -> consultaService.agendar(nova))
				.isInstanceOf(RecursoNaoEncontradoException.class);
	}

	@Test
	void confirmarDeveAlterarStatus() {
		when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta(StatusConsulta.AGENDADA)));
		when(consultaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		assertThat(consultaService.confirmar(1L).status()).isEqualTo(StatusConsulta.CONFIRMADA);
	}

	@Test
	void registrarFaltaDeveAlterarStatus() {
		when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta(StatusConsulta.CONFIRMADA)));
		when(consultaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		assertThat(consultaService.registrarFalta(1L).status()).isEqualTo(StatusConsulta.FALTOU);
	}

	@Test
	void registrarComparecimentoDeveAlterarStatusParaRealizada() {
		when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta(StatusConsulta.CONFIRMADA)));
		when(consultaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		assertThat(consultaService.registrarComparecimento(1L).status()).isEqualTo(StatusConsulta.REALIZADA);
	}

	@Test
	void cancelarDeveReagendarProximoDaListaDeEspera() {
		when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta(StatusConsulta.AGENDADA)));
		when(consultaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
		ListaEspera proximo = new ListaEspera(9L, 7L, "CARDIOLOGIA", 1, OffsetDateTime.now());
		when(listaEsperaRepository.findNextByEspecialidade("CARDIOLOGIA")).thenReturn(Optional.of(proximo));

		Consulta cancelada = consultaService.cancelar(1L);

		assertThat(cancelada.status()).isEqualTo(StatusConsulta.CANCELADA);
		// 1x cancelamento + 1x reagendamento
		verify(consultaRepository, times(2)).save(any());
		verify(historicoRepository).save(any(HistoricoListaEspera.class));
		verify(listaEsperaRepository).deleteById(9L);
	}

	@Test
	void cancelarSemListaDeEsperaNaoDeveReagendar() {
		when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta(StatusConsulta.AGENDADA)));
		when(consultaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
		when(listaEsperaRepository.findNextByEspecialidade("CARDIOLOGIA")).thenReturn(Optional.empty());

		consultaService.cancelar(1L);

		verify(consultaRepository, times(1)).save(any());
		verify(historicoRepository, never()).save(any());
		verify(listaEsperaRepository, never()).deleteById(any());
	}

	@Test
	void buscarConsultaInexistenteDeveFalhar() {
		when(consultaRepository.findById(404L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> consultaService.confirmar(404L))
				.isInstanceOf(RecursoNaoEncontradoException.class);
	}
}
