package com.fase5.hackton.application.port;

import com.fase5.hackton.domain.model.Paciente;
import java.util.List;
import java.util.Optional;

public interface PacienteRepositoryPort {
	Paciente save(Paciente paciente);
	Optional<Paciente> findById(Long id);
	Optional<Paciente> findByCpf(String cpf);
	List<Paciente> findAll();
	void deleteById(Long id);
	boolean existsByCpf(String cpf);
	boolean existsByCartaoSus(String cartaoSus);
	long count();
}
