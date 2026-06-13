package com.fase5.hackton.infrastructure.persistence.adapter;

import com.fase5.hackton.application.port.FilaRepositoryPort;
import com.fase5.hackton.domain.model.ClassificacaoRisco;
import com.fase5.hackton.domain.model.FilaAtendimento;
import com.fase5.hackton.domain.model.StatusFila;
import com.fase5.hackton.infrastructure.persistence.mapper.PersistenceMapper;
import com.fase5.hackton.infrastructure.persistence.repository.SpringFilaRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class FilaRepositoryAdapter implements FilaRepositoryPort {
	private final SpringFilaRepository repository;

	public FilaRepositoryAdapter(SpringFilaRepository repository) {
		this.repository = repository;
	}

	@Override
	public FilaAtendimento save(FilaAtendimento filaAtendimento) {
		return PersistenceMapper.toDomain(repository.save(PersistenceMapper.toEntity(filaAtendimento)));
	}

	@Override
	public Optional<FilaAtendimento> findById(Long id) {
		return repository.findById(id).map(PersistenceMapper::toDomain);
	}

	@Override
	public List<FilaAtendimento> findByUnidadeAndStatus(Long unidadeSaudeId, StatusFila status) {
		return repository.findByUnidadeSaudeIdAndStatus(unidadeSaudeId, status).stream()
				.map(PersistenceMapper::toDomain)
				.toList();
	}

	@Override
	public long countBefore(Long unidadeSaudeId, StatusFila status, ClassificacaoRisco risco, OffsetDateTime horarioEntrada) {
		return findByUnidadeAndStatus(unidadeSaudeId, status).stream()
				.filter(item -> item.classificacaoRisco().prioridade() < risco.prioridade()
						|| (item.classificacaoRisco() == risco && item.horarioEntrada().isBefore(horarioEntrada)))
				.count();
	}

	@Override
	public long countByStatus(StatusFila status) {
		return repository.countByStatus(status);
	}
}
