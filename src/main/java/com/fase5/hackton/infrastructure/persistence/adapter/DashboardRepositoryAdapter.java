package com.fase5.hackton.infrastructure.persistence.adapter;

import com.fase5.hackton.application.port.DashboardRepositoryPort;
import com.fase5.hackton.domain.model.ClassificacaoRisco;
import com.fase5.hackton.infrastructure.persistence.repository.SpringFilaRepository;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardRepositoryAdapter implements DashboardRepositoryPort {
	private final SpringFilaRepository filaRepository;

	public DashboardRepositoryAdapter(SpringFilaRepository filaRepository) {
		this.filaRepository = filaRepository;
	}

	@Override
	public Map<ClassificacaoRisco, Long> countAtendimentosPorClassificacaoRisco() {
		Map<ClassificacaoRisco, Long> resultado = new EnumMap<>(ClassificacaoRisco.class);
		for (Object[] linha : filaRepository.countAtendimentosPorClassificacao()) {
			resultado.put((ClassificacaoRisco) linha[0], (Long) linha[1]);
		}
		return resultado;
	}
}
