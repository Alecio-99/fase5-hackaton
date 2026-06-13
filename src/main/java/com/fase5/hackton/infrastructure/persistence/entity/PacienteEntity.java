package com.fase5.hackton.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "pacientes")
public class PacienteEntity extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String nome;
	@Column(nullable = false, unique = true, length = 11)
	private String cpf;
	@Column(nullable = false)
	private LocalDate dataNascimento;
	@Column(nullable = false)
	private String telefone;
	private String email;
	@Column(nullable = false)
	private String endereco;
	@Column(nullable = false, unique = true)
	private String cartaoSus;
	@Column(nullable = false)
	private OffsetDateTime dataCadastro;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	public String getCpf() { return cpf; }
	public void setCpf(String cpf) { this.cpf = cpf; }
	public LocalDate getDataNascimento() { return dataNascimento; }
	public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
	public String getTelefone() { return telefone; }
	public void setTelefone(String telefone) { this.telefone = telefone; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getEndereco() { return endereco; }
	public void setEndereco(String endereco) { this.endereco = endereco; }
	public String getCartaoSus() { return cartaoSus; }
	public void setCartaoSus(String cartaoSus) { this.cartaoSus = cartaoSus; }
	public OffsetDateTime getDataCadastro() { return dataCadastro; }
	public void setDataCadastro(OffsetDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}
