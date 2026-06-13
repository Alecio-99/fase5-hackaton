package com.fase5.hackton.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "lista_espera")
public class ListaEsperaEntity extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private Long pacienteId;
	@Column(nullable = false)
	private String especialidade;
	@Column(nullable = false)
	private Integer prioridade;
	@Column(nullable = false)
	private OffsetDateTime dataCadastro;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getPacienteId() { return pacienteId; }
	public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
	public String getEspecialidade() { return especialidade; }
	public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
	public Integer getPrioridade() { return prioridade; }
	public void setPrioridade(Integer prioridade) { this.prioridade = prioridade; }
	public OffsetDateTime getDataCadastro() { return dataCadastro; }
	public void setDataCadastro(OffsetDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}
