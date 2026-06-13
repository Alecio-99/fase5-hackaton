package com.fase5.hackton.application.service;

import com.fase5.hackton.application.port.UnidadeSaudeRepositoryPort;
import com.fase5.hackton.domain.exception.RecursoNaoEncontradoException;
import com.fase5.hackton.domain.model.UnidadeSaude;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UnidadeSaudeService {
	private final UnidadeSaudeRepositoryPort unidadeRepository;

	public UnidadeSaudeService(UnidadeSaudeRepositoryPort unidadeRepository) {
		this.unidadeRepository = unidadeRepository;
	}

	@Transactional
	public UnidadeSaude criar(UnidadeSaude unidadeSaude) {
		return unidadeRepository.save(new UnidadeSaude(null, unidadeSaude.nome(), unidadeSaude.tipo(),
				unidadeSaude.endereco(), unidadeSaude.telefone(), unidadeSaude.capacidadeAtendimento()));
	}

	@Transactional(readOnly = true)
	public List<UnidadeSaude> listar() {
		return unidadeRepository.findAll();
	}

	@Transactional(readOnly = true)
	public UnidadeSaude buscarPorId(Long id) {
		return unidadeRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Unidade de saude nao encontrada"));
	}

	@Transactional
	public UnidadeSaude atualizar(Long id, UnidadeSaude unidadeSaude) {
		buscarPorId(id);
		return unidadeRepository.save(new UnidadeSaude(id, unidadeSaude.nome(), unidadeSaude.tipo(),
				unidadeSaude.endereco(), unidadeSaude.telefone(), unidadeSaude.capacidadeAtendimento()));
	}

	@Transactional
	public void excluir(Long id) {
		buscarPorId(id);
		unidadeRepository.deleteById(id);
	}
}
