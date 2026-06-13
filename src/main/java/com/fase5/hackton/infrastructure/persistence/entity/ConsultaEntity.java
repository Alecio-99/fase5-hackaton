package com.fase5.hackton.infrastructure.persistence.entity;

import com.fase5.hackton.domain.model.StatusConsulta;
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
@Table(name = "consultas")
public class ConsultaEntity extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private Long pacienteId;
	@Column(nullable = false)
	private Long unidadeSaudeId;
	@Column(nullable = false)
	private OffsetDateTime dataHoraConsulta;
	@Column(nullable = false)
	private String especialidade;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusConsulta status;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getPacienteId() { return pacienteId; }
	public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
	public Long getUnidadeSaudeId() { return unidadeSaudeId; }
	public void setUnidadeSaudeId(Long unidadeSaudeId) { this.unidadeSaudeId = unidadeSaudeId; }
	public OffsetDateTime getDataHoraConsulta() { return dataHoraConsulta; }
	public void setDataHoraConsulta(OffsetDateTime dataHoraConsulta) { this.dataHoraConsulta = dataHoraConsulta; }
	public String getEspecialidade() { return especialidade; }
	public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
	public StatusConsulta getStatus() { return status; }
	public void setStatus(StatusConsulta status) { this.status = status; }
}
