package com.fase5.hackton.infrastructure.persistence.adapter;

import com.fase5.hackton.application.port.PacienteRepositoryPort;
import com.fase5.hackton.domain.model.Paciente;
import com.fase5.hackton.infrastructure.persistence.mapper.PersistenceMapper;
import com.fase5.hackton.infrastructure.persistence.repository.SpringPacienteRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PacienteRepositoryAdapter implements PacienteRepositoryPort {
	private final SpringPacienteRepository repository;

	public PacienteRepositoryAdapter(SpringPacienteRepository repository) {
		this.repository = repository;
	}

	@Override
	public Paciente save(Paciente paciente) {
		return PersistenceMapper.toDomain(repository.save(PersistenceMapper.toEntity(paciente)));
	}

	@Override
	public Optional<Paciente> findById(Long id) {
		return repository.findById(id).map(PersistenceMapper::toDomain);
	}

	@Override
	public Optional<Paciente> findByCpf(String cpf) {
		return repository.findByCpf(cpf).map(PersistenceMapper::toDomain);
	}

	@Override
	public List<Paciente> findAll() {
		return repository.findAll().stream().map(PersistenceMapper::toDomain).toList();
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	@Override
	public boolean existsByCpf(String cpf) {
		return repository.existsByCpf(cpf);
	}

	@Override
	public boolean existsByCartaoSus(String cartaoSus) {
		return repository.existsByCartaoSus(cartaoSus);
	}

	@Override
	public long count() {
		return repository.count();
	}
}
