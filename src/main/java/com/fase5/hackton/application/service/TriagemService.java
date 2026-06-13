package com.fase5.hackton.application.service;

import com.fase5.hackton.application.port.PacienteRepositoryPort;
import com.fase5.hackton.application.port.TriagemRepositoryPort;
import com.fase5.hackton.domain.exception.RecursoNaoEncontradoException;
import com.fase5.hackton.domain.model.Triagem;
import com.fase5.hackton.domain.service.ClassificadorRiscoService;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TriagemService {
	private final TriagemRepositoryPort triagemRepository;
	private final PacienteRepositoryPort pacienteRepository;
	private final ClassificadorRiscoService classificadorRiscoService;

	public TriagemService(TriagemRepositoryPort triagemRepository, PacienteRepositoryPort pacienteRepository) {
		this.triagemRepository = triagemRepository;
		this.pacienteRepository = pacienteRepository;
		this.classificadorRiscoService = new ClassificadorRiscoService();
	}

	@Transactional
	public Triagem criar(Triagem triagem) {
		pacienteRepository.findById(triagem.pacienteId())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Paciente nao encontrado"));
		return triagemRepository.save(new Triagem(null, triagem.pacienteId(), OffsetDateTime.now(), triagem.respostas(),
				classificadorRiscoService.classificar(triagem.respostas()), triagem.observacoes()));
	}

	@Transactional(readOnly = true)
	public Triagem buscarPorId(Long id) {
		return triagemRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Triagem nao encontrada"));
	}
}
