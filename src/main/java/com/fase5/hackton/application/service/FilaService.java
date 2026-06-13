package com.fase5.hackton.application.service;

import com.fase5.hackton.application.port.FilaRepositoryPort;
import com.fase5.hackton.application.port.PacienteRepositoryPort;
import com.fase5.hackton.application.port.TriagemRepositoryPort;
import com.fase5.hackton.application.port.UnidadeSaudeRepositoryPort;
import com.fase5.hackton.domain.exception.RecursoNaoEncontradoException;
import com.fase5.hackton.domain.exception.RegraNegocioException;
import com.fase5.hackton.domain.model.FilaAtendimento;
import com.fase5.hackton.domain.model.StatusFila;
import com.fase5.hackton.domain.model.Triagem;
import java.time.OffsetDateTime;
import java.util.Comparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FilaService {
	private final FilaRepositoryPort filaRepository;
	private final PacienteRepositoryPort pacienteRepository;
	private final UnidadeSaudeRepositoryPort unidadeRepository;
	private final TriagemRepositoryPort triagemRepository;

	public FilaService(FilaRepositoryPort filaRepository, PacienteRepositoryPort pacienteRepository,
			UnidadeSaudeRepositoryPort unidadeRepository, TriagemRepositoryPort triagemRepository) {
		this.filaRepository = filaRepository;
		this.pacienteRepository = pacienteRepository;
		this.unidadeRepository = unidadeRepository;
		this.triagemRepository = triagemRepository;
	}

	@Transactional
	public FilaAtendimento entrarNaFila(Long pacienteId, Long unidadeSaudeId, Long triagemId) {
		pacienteRepository.findById(pacienteId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Paciente nao encontrado"));
		unidadeRepository.findById(unidadeSaudeId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Unidade de saude nao encontrada"));
		Triagem triagem = triagemRepository.findById(triagemId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Triagem nao encontrada"));
		if (!triagem.pacienteId().equals(pacienteId)) {
			throw new RegraNegocioException("Triagem nao pertence ao paciente informado");
		}
		return filaRepository.save(new FilaAtendimento(null, pacienteId, unidadeSaudeId, triagem.classificacaoRisco(),
				OffsetDateTime.now(), StatusFila.AGUARDANDO));
	}

	@Transactional(readOnly = true)
	public int consultarPosicao(Long filaId) {
		FilaAtendimento fila = buscarPorId(filaId);
		if (fila.status() != StatusFila.AGUARDANDO) {
			return 0;
		}
		return (int) filaRepository.countBefore(fila.unidadeSaudeId(), StatusFila.AGUARDANDO,
				fila.classificacaoRisco(), fila.horarioEntrada()) + 1;
	}

	@Transactional
	public FilaAtendimento chamarProximo(Long unidadeSaudeId) {
		return filaRepository.findByUnidadeAndStatus(unidadeSaudeId, StatusFila.AGUARDANDO).stream()
				.min(Comparator.comparing((FilaAtendimento item) -> item.classificacaoRisco().prioridade())
						.thenComparing(FilaAtendimento::horarioEntrada))
				.map(item -> filaRepository.save(item.comStatus(StatusFila.EM_ATENDIMENTO)))
				.orElseThrow(() -> new RecursoNaoEncontradoException("Nao ha pacientes aguardando"));
	}

	@Transactional
	public FilaAtendimento finalizar(Long id) {
		return filaRepository.save(buscarPorId(id).comStatus(StatusFila.FINALIZADO));
	}

	@Transactional
	public FilaAtendimento cancelar(Long id) {
		return filaRepository.save(buscarPorId(id).comStatus(StatusFila.CANCELADO));
	}

	private FilaAtendimento buscarPorId(Long id) {
		return filaRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Registro de fila nao encontrado"));
	}
}
