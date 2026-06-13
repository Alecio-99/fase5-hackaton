package com.fase5.hackton.infrastructure.persistence.entity;

import com.fase5.hackton.domain.model.ClassificacaoRisco;
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
@Table(name = "triagens")
public class TriagemEntity extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private Long pacienteId;
	@Column(nullable = false)
	private OffsetDateTime dataTriagem;
	private boolean possuiFebre;
	private boolean possuiFaltaDeAr;
	private boolean possuiDorIntensa;
	private boolean possuiSangramento;
	private boolean possuiTontura;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ClassificacaoRisco classificacaoRisco;
	@Column(length = 1000)
	private String observacoes;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getPacienteId() { return pacienteId; }
	public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
	public OffsetDateTime getDataTriagem() { return dataTriagem; }
	public void setDataTriagem(OffsetDateTime dataTriagem) { this.dataTriagem = dataTriagem; }
	public boolean isPossuiFebre() { return possuiFebre; }
	public void setPossuiFebre(boolean possuiFebre) { this.possuiFebre = possuiFebre; }
	public boolean isPossuiFaltaDeAr() { return possuiFaltaDeAr; }
	public void setPossuiFaltaDeAr(boolean possuiFaltaDeAr) { this.possuiFaltaDeAr = possuiFaltaDeAr; }
	public boolean isPossuiDorIntensa() { return possuiDorIntensa; }
	public void setPossuiDorIntensa(boolean possuiDorIntensa) { this.possuiDorIntensa = possuiDorIntensa; }
	public boolean isPossuiSangramento() { return possuiSangramento; }
	public void setPossuiSangramento(boolean possuiSangramento) { this.possuiSangramento = possuiSangramento; }
	public boolean isPossuiTontura() { return possuiTontura; }
	public void setPossuiTontura(boolean possuiTontura) { this.possuiTontura = possuiTontura; }
	public ClassificacaoRisco getClassificacaoRisco() { return classificacaoRisco; }
	public void setClassificacaoRisco(ClassificacaoRisco classificacaoRisco) { this.classificacaoRisco = classificacaoRisco; }
	public String getObservacoes() { return observacoes; }
	public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
