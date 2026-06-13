package com.fase5.hackton.application.port;

import com.fase5.hackton.domain.model.UnidadeSaude;
import java.util.List;
import java.util.Optional;

public interface UnidadeSaudeRepositoryPort {
	UnidadeSaude save(UnidadeSaude unidadeSaude);
	Optional<UnidadeSaude> findById(Long id);
	List<UnidadeSaude> findAll();
	void deleteById(Long id);
}
