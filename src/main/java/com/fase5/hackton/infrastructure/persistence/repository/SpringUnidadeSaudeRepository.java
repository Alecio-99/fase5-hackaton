package com.fase5.hackton.infrastructure.persistence.repository;

import com.fase5.hackton.infrastructure.persistence.entity.UnidadeSaudeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringUnidadeSaudeRepository extends JpaRepository<UnidadeSaudeEntity, Long> {
}
