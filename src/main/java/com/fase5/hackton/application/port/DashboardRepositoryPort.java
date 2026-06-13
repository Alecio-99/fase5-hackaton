package com.fase5.hackton.application.port;

import com.fase5.hackton.domain.model.ClassificacaoRisco;
import java.util.Map;

public interface DashboardRepositoryPort {
	Map<ClassificacaoRisco, Long> countAtendimentosPorClassificacaoRisco();
}
