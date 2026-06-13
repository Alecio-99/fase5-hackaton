package com.fase5.hackton.infrastructure.persistence.adapter;

import com.fase5.hackton.application.port.TriagemRepositoryPort;
import com.fase5.hackton.domain.model.Triagem;
import com.fase5.hackton.infrastructure.persistence.mapper.PersistenceMapper;
import com.fase5.hackton.infrastructure.persistence.repository.SpringTriagemRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TriagemRepositoryAdapter implements TriagemRepositoryPort {
	private final SpringTriagemRepository repository;

	public TriagemRepositoryAdapter(SpringTriagemRepository repository) {
		this.repository = repository;
	}

	@Override
	public Triagem save(Triagem triagem) {
		return PersistenceMapper.toDomain(repository.save(PersistenceMapper.toEntity(triagem)));
	}

	@Override
	public Optional<Triagem> findById(Long id) {
		return repository.findById(id).map(PersistenceMapper::toDomain);
	}
}
