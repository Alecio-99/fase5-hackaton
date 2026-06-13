package com.fase5.hackton.infrastructure.persistence.entity;

import com.fase5.hackton.domain.model.TipoUnidade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "unidades_saude")
public class UnidadeSaudeEntity extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String nome;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoUnidade tipo;
	@Column(nullable = false)
	private String endereco;
	@Column(nullable = false)
	private String telefone;
	@Column(nullable = false)
	private Integer capacidadeAtendimento;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	public TipoUnidade getTipo() { return tipo; }
	public void setTipo(TipoUnidade tipo) { this.tipo = tipo; }
	public String getEndereco() { return endereco; }
	public void setEndereco(String endereco) { this.endereco = endereco; }
	public String getTelefone() { return telefone; }
	public void setTelefone(String telefone) { this.telefone = telefone; }
	public Integer getCapacidadeAtendimento() { return capacidadeAtendimento; }
	public void setCapacidadeAtendimento(Integer capacidadeAtendimento) { this.capacidadeAtendimento = capacidadeAtendimento; }
}
