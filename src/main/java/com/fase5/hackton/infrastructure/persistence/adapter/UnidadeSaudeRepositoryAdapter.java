package com.fase5.hackton.infrastructure.persistence.adapter;

import com.fase5.hackton.application.port.UnidadeSaudeRepositoryPort;
import com.fase5.hackton.domain.model.UnidadeSaude;
import com.fase5.hackton.infrastructure.persistence.mapper.PersistenceMapper;
import com.fase5.hackton.infrastructure.persistence.repository.SpringUnidadeSaudeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UnidadeSaudeRepositoryAdapter implements UnidadeSaudeRepositoryPort {
	private final SpringUnidadeSaudeRepository repository;

	public UnidadeSaudeRepositoryAdapter(SpringUnidadeSaudeRepository repository) {
		this.repository = repository;
	}

	@Override
	public UnidadeSaude save(UnidadeSaude unidadeSaude) {
		return PersistenceMapper.toDomain(repository.save(PersistenceMapper.toEntity(unidadeSaude)));
	}

	@Override
	public Optional<UnidadeSaude> findById(Long id) {
		return repository.findById(id).map(PersistenceMapper::toDomain);
	}

	@Override
	public List<UnidadeSaude> findAll() {
		return repository.findAll().stream().map(PersistenceMapper::toDomain).toList();
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
