package com.fase5.hackton.domain.model;

import java.util.Map;

public record DashboardStats(
		long quantidadePacientes,
		long consultasAgendadas,
		long consultasRealizadas,
		long consultasPerdidas,
		double taxaAbsenteismo,
		long pacientesEmFila,
		Map<ClassificacaoRisco, Long> atendimentosPorClassificacaoRisco
) {
}
