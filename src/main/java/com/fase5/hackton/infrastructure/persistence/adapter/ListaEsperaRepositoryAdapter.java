package com.fase5.hackton.infrastructure.persistence.adapter;

import com.fase5.hackton.application.port.ListaEsperaRepositoryPort;
import com.fase5.hackton.domain.model.ListaEspera;
import com.fase5.hackton.infrastructure.persistence.mapper.PersistenceMapper;
import com.fase5.hackton.infrastructure.persistence.repository.SpringListaEsperaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ListaEsperaRepositoryAdapter implements ListaEsperaRepositoryPort {
	private final SpringListaEsperaRepository repository;

	public ListaEsperaRepositoryAdapter(SpringListaEsperaRepository repository) {
		this.repository = repository;
	}

	@Override
	public ListaEspera save(ListaEspera listaEspera) {
		return PersistenceMapper.toDomain(repository.save(PersistenceMapper.toEntity(listaEspera)));
	}

	@Override
	public List<ListaEspera> findAll() {
		return repository.findAll().stream().map(PersistenceMapper::toDomain).toList();
	}

	@Override
	public Optional<ListaEspera> findNextByEspecialidade(String especialidade) {
		return repository.findFirstByEspecialidadeOrderByPrioridadeAscDataCadastroAsc(especialidade)
				.map(PersistenceMapper::toDomain);
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
