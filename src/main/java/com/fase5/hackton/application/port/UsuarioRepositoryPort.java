package com.fase5.hackton.application.port;

import com.fase5.hackton.domain.model.Usuario;
import java.util.Optional;

public interface UsuarioRepositoryPort {
	Usuario save(Usuario usuario);
	Optional<Usuario> findByEmail(String email);
	boolean existsByEmail(String email);
	long count();
}
