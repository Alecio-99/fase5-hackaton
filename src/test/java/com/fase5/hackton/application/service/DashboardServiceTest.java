package com.fase5.hackton.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.fase5.hackton.application.port.ConsultaRepositoryPort;
import com.fase5.hackton.application.port.DashboardRepositoryPort;
import com.fase5.hackton.application.port.FilaRepositoryPort;
import com.fase5.hackton.application.port.PacienteRepositoryPort;
import com.fase5.hackton.domain.model.ClassificacaoRisco;
import com.fase5.hackton.domain.model.DashboardStats;
import com.fase5.hackton.domain.model.StatusConsulta;
import com.fase5.hackton.domain.model.StatusFila;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

	@Mock
	private PacienteRepositoryPort pacienteRepository;
	@Mock
	private ConsultaRepositoryPort consultaRepository;
	@Mock
	private FilaRepositoryPort filaRepository;
	@Mock
	private DashboardRepositoryPort dashboardRepository;

	@InjectMocks
	private DashboardService dashboardService;

	@Test
	void deveCalcularTaxaDeAbsenteismoSobreConsultasConcluidas() {
		when(consultaRepository.countByStatus(StatusConsulta.AGENDADA)).thenReturn(4L);
		when(consultaRepository.countByStatus(StatusConsulta.REALIZADA)).thenReturn(6L);
		when(consultaRepository.countByStatus(StatusConsulta.FALTOU)).thenReturn(2L);
		when(pacienteRepository.count()).thenReturn(100L);
		when(filaRepository.countByStatus(StatusFila.AGUARDANDO)).thenReturn(5L);
		when(dashboardRepository.countAtendimentosPorClassificacaoRisco())
				.thenReturn(Map.of(ClassificacaoRisco.VERMELHO, 3L));

		DashboardStats stats = dashboardService.obter();

		// perdidas=2, realizadas=6 -> 2/(2+6) = 25%
		assertThat(stats.taxaAbsenteismo()).isEqualTo(25.0);
		assertThat(stats.quantidadePacientes()).isEqualTo(100L);
		assertThat(stats.consultasAgendadas()).isEqualTo(4L);
		assertThat(stats.pacientesEmFila()).isEqualTo(5L);
		assertThat(stats.atendimentosPorClassificacaoRisco()).containsEntry(ClassificacaoRisco.VERMELHO, 3L);
	}

	@Test
	void taxaDeAbsenteismoDeveSerZeroQuandoNaoHaConsultasConcluidas() {
		when(consultaRepository.countByStatus(StatusConsulta.AGENDADA)).thenReturn(10L);
		when(consultaRepository.countByStatus(StatusConsulta.REALIZADA)).thenReturn(0L);
		when(consultaRepository.countByStatus(StatusConsulta.FALTOU)).thenReturn(0L);
		when(pacienteRepository.count()).thenReturn(0L);
		when(filaRepository.countByStatus(StatusFila.AGUARDANDO)).thenReturn(0L);
		when(dashboardRepository.countAtendimentosPorClassificacaoRisco()).thenReturn(Map.of());

		assertThat(dashboardService.obter().taxaAbsenteismo()).isZero();
	}
}
