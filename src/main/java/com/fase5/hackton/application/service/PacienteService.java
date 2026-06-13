package com.fase5.hackton.application.service;

import com.fase5.hackton.application.port.PacienteRepositoryPort;
import com.fase5.hackton.domain.exception.RecursoNaoEncontradoException;
import com.fase5.hackton.domain.exception.RegraNegocioException;
import com.fase5.hackton.domain.model.Paciente;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {
	private final PacienteRepositoryPort pacienteRepository;

	public PacienteService(PacienteRepositoryPort pacienteRepository) {
		this.pacienteRepository = pacienteRepository;
	}

	@Transactional
	public Paciente criar(Paciente paciente) {
		validarUnicidade(paciente, null);
		return pacienteRepository.save(new Paciente(null, paciente.nome(), paciente.cpf(), paciente.dataNascimento(),
				paciente.telefone(), paciente.email(), paciente.endereco(), paciente.cartaoSus(), OffsetDateTime.now()));
	}

	@Transactional(readOnly = true)
	public List<Paciente> listar() {
		return pacienteRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Paciente buscarPorId(Long id) {
		return pacienteRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Paciente nao encontrado"));
	}

	@Transactional(readOnly = true)
	public Paciente buscarPorCpf(String cpf) {
		return pacienteRepository.findByCpf(cpf)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Paciente nao encontrado"));
	}

	@Transactional
	public Paciente atualizar(Long id, Paciente paciente) {
		Paciente atual = buscarPorId(id);
		validarUnicidade(paciente, id);
		return pacienteRepository.save(new Paciente(id, paciente.nome(), paciente.cpf(), paciente.dataNascimento(),
				paciente.telefone(), paciente.email(), paciente.endereco(), paciente.cartaoSus(), atual.dataCadastro()));
	}

	@Transactional
	public void excluir(Long id) {
		buscarPorId(id);
		pacienteRepository.deleteById(id);
	}

	private void validarUnicidade(Paciente paciente, Long idAtual) {
		pacienteRepository.findByCpf(paciente.cpf())
				.filter(existente -> !existente.id().equals(idAtual))
				.ifPresent(existente -> {
					throw new RegraNegocioException("CPF ja cadastrado");
				});
		if (idAtual == null && pacienteRepository.existsByCartaoSus(paciente.cartaoSus())) {
			throw new RegraNegocioException("Cartao SUS ja cadastrado");
		}
	}
}
