package com.fase5.hackton.infrastructure.persistence.repository;

import com.fase5.hackton.infrastructure.persistence.entity.TriagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringTriagemRepository extends JpaRepository<TriagemEntity, Long> {
}
