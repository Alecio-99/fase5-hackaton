package com.fase5.hackton.application.port;

import com.fase5.hackton.domain.model.Triagem;
import java.util.Optional;

public interface TriagemRepositoryPort {
	Triagem save(Triagem triagem);
	Optional<Triagem> findById(Long id);
}
