package com.fase5.hackton.infrastructure.persistence.repository;

import com.fase5.hackton.infrastructure.persistence.entity.UsuarioEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
	Optional<UsuarioEntity> findByEmail(String email);
	boolean existsByEmail(String email);
}
