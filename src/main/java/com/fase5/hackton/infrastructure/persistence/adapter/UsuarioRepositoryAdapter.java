package com.fase5.hackton.infrastructure.persistence.adapter;

import com.fase5.hackton.application.port.UsuarioRepositoryPort;
import com.fase5.hackton.domain.model.Usuario;
import com.fase5.hackton.infrastructure.persistence.mapper.PersistenceMapper;
import com.fase5.hackton.infrastructure.persistence.repository.SpringUsuarioRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {
	private final SpringUsuarioRepository repository;

	public UsuarioRepositoryAdapter(SpringUsuarioRepository repository) {
		this.repository = repository;
	}

	@Override
	public Usuario save(Usuario usuario) {
		return PersistenceMapper.toDomain(repository.save(PersistenceMapper.toEntity(usuario)));
	}

	@Override
	public Optional<Usuario> findByEmail(String email) {
		return repository.findByEmail(email).map(PersistenceMapper::toDomain);
	}

	@Override
	public boolean existsByEmail(String email) {
		return repository.existsByEmail(email);
	}

	@Override
	public long count() {
		return repository.count();
	}
}
