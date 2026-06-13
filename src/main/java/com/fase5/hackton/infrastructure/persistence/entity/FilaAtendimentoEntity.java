package com.fase5.hackton.infrastructure.persistence.entity;

import com.fase5.hackton.domain.model.ClassificacaoRisco;
import com.fase5.hackton.domain.model.StatusFila;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "fila_atendimento")
public class FilaAtendimentoEntity extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private Long pacienteId;
	@Column(nullable = false)
	private Long unidadeSaudeId;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ClassificacaoRisco classificacaoRisco;
	@Column(nullable = false)
	private OffsetDateTime horarioEntrada;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusFila status;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getPacienteId() { return pacienteId; }
	public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
	public Long getUnidadeSaudeId() { return unidadeSaudeId; }
	public void setUnidadeSaudeId(Long unidadeSaudeId) { this.unidadeSaudeId = unidadeSaudeId; }
	public ClassificacaoRisco getClassificacaoRisco() { return classificacaoRisco; }
	public void setClassificacaoRisco(ClassificacaoRisco classificacaoRisco) { this.classificacaoRisco = classificacaoRisco; }
	public OffsetDateTime getHorarioEntrada() { return horarioEntrada; }
	public void setHorarioEntrada(OffsetDateTime horarioEntrada) { this.horarioEntrada = horarioEntrada; }
	public StatusFila getStatus() { return status; }
	public void setStatus(StatusFila status) { this.status = status; }
}
