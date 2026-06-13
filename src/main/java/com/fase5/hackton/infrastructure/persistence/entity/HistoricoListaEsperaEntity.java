package com.fase5.hackton.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "historico_lista_espera")
public class HistoricoListaEsperaEntity extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private Long pacienteId;
	private Long consultaId;
	@Column(nullable = false)
	private String especialidade;
	@Column(nullable = false, length = 1000)
	private String descricao;
	@Column(nullable = false)
	private OffsetDateTime dataMovimentacao;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getPacienteId() { return pacienteId; }
	public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
	public Long getConsultaId() { return consultaId; }
	public void setConsultaId(Long consultaId) { this.consultaId = consultaId; }
	public String getEspecialidade() { return especialidade; }
	public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
	public String getDescricao() { return descricao; }
	public void setDescricao(String descricao) { this.descricao = descricao; }
	public OffsetDateTime getDataMovimentacao() { return dataMovimentacao; }
	public void setDataMovimentacao(OffsetDateTime dataMovimentacao) { this.dataMovimentacao = dataMovimentacao; }
}
