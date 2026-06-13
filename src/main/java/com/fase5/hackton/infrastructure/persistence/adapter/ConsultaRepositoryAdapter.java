package com.fase5.hackton.infrastructure.persistence.adapter;

import com.fase5.hackton.application.port.ConsultaRepositoryPort;
import com.fase5.hackton.domain.model.Consulta;
import com.fase5.hackton.domain.model.StatusConsulta;
import com.fase5.hackton.infrastructure.persistence.mapper.PersistenceMapper;
import com.fase5.hackton.infrastructure.persistence.repository.SpringConsultaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ConsultaRepositoryAdapter implements ConsultaRepositoryPort {
	private final SpringConsultaRepository repository;

	public ConsultaRepositoryAdapter(SpringConsultaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Consulta save(Consulta consulta) {
		return PersistenceMapper.toDomain(repository.save(PersistenceMapper.toEntity(consulta)));
	}

	@Override
	public Optional<Consulta> findById(Long id) {
		return repository.findById(id).map(PersistenceMapper::toDomain);
	}

	@Override
	public long countByStatus(StatusConsulta status) {
		return repository.countByStatus(status);
	}
}
