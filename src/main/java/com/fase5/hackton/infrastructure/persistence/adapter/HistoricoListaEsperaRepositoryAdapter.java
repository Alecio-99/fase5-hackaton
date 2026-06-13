package com.fase5.hackton.infrastructure.persistence.adapter;

import com.fase5.hackton.application.port.HistoricoListaEsperaRepositoryPort;
import com.fase5.hackton.domain.model.HistoricoListaEspera;
import com.fase5.hackton.infrastructure.persistence.mapper.PersistenceMapper;
import com.fase5.hackton.infrastructure.persistence.repository.SpringHistoricoListaEsperaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HistoricoListaEsperaRepositoryAdapter implements HistoricoListaEsperaRepositoryPort {
	private final SpringHistoricoListaEsperaRepository repository;

	public HistoricoListaEsperaRepositoryAdapter(SpringHistoricoListaEsperaRepository repository) {
		this.repository = repository;
	}

	@Override
	public HistoricoListaEspera save(HistoricoListaEspera historico) {
		return PersistenceMapper.toDomain(repository.save(PersistenceMapper.toEntity(historico)));
	}
}
