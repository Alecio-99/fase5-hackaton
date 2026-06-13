package com.fase5.hackton.application.port;

import com.fase5.hackton.domain.model.Consulta;
import com.fase5.hackton.domain.model.StatusConsulta;
import java.util.Optional;

public interface ConsultaRepositoryPort {
	Consulta save(Consulta consulta);
	Optional<Consulta> findById(Long id);
	long countByStatus(StatusConsulta status);
}
