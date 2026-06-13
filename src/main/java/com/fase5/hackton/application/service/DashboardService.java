package com.fase5.hackton.application.service;

import com.fase5.hackton.application.port.ConsultaRepositoryPort;
import com.fase5.hackton.application.port.DashboardRepositoryPort;
import com.fase5.hackton.application.port.FilaRepositoryPort;
import com.fase5.hackton.application.port.PacienteRepositoryPort;
import com.fase5.hackton.domain.model.DashboardStats;
import com.fase5.hackton.domain.model.StatusConsulta;
import com.fase5.hackton.domain.model.StatusFila;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {
	private final PacienteRepositoryPort pacienteRepository;
	private final ConsultaRepositoryPort consultaRepository;
	private final FilaRepositoryPort filaRepository;
	private final DashboardRepositoryPort dashboardRepository;

	public DashboardService(PacienteRepositoryPort pacienteRepository, ConsultaRepositoryPort consultaRepository,
			FilaRepositoryPort filaRepository, DashboardRepositoryPort dashboardRepository) {
		this.pacienteRepository = pacienteRepository;
		this.consultaRepository = consultaRepository;
		this.filaRepository = filaRepository;
		this.dashboardRepository = dashboardRepository;
	}

	@Transactional(readOnly = true)
	public DashboardStats obter() {
		long agendadas = consultaRepository.countByStatus(StatusConsulta.AGENDADA);
		long realizadas = consultaRepository.countByStatus(StatusConsulta.REALIZADA);
		long perdidas = consultaRepository.countByStatus(StatusConsulta.FALTOU);
		long totalConcluidas = realizadas + perdidas;
		double taxaAbsenteismo = totalConcluidas == 0 ? 0 : (perdidas * 100.0) / totalConcluidas;
		return new DashboardStats(pacienteRepository.count(), agendadas, realizadas, perdidas, taxaAbsenteismo,
				filaRepository.countByStatus(StatusFila.AGUARDANDO),
				dashboardRepository.countAtendimentosPorClassificacaoRisco());
	}
}
