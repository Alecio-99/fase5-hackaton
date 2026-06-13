package com.fase5.hackton.application.service;

import com.fase5.hackton.application.port.ConsultaRepositoryPort;
import com.fase5.hackton.application.port.HistoricoListaEsperaRepositoryPort;
import com.fase5.hackton.application.port.ListaEsperaRepositoryPort;
import com.fase5.hackton.application.port.PacienteRepositoryPort;
import com.fase5.hackton.application.port.UnidadeSaudeRepositoryPort;
import com.fase5.hackton.domain.exception.RecursoNaoEncontradoException;
import com.fase5.hackton.domain.model.Consulta;
import com.fase5.hackton.domain.model.HistoricoListaEspera;
import com.fase5.hackton.domain.model.ListaEspera;
import com.fase5.hackton.domain.model.StatusConsulta;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {
	private final ConsultaRepositoryPort consultaRepository;
	private final PacienteRepositoryPort pacienteRepository;
	private final UnidadeSaudeRepositoryPort unidadeRepository;
	private final ListaEsperaRepositoryPort listaEsperaRepository;
	private final HistoricoListaEsperaRepositoryPort historicoRepository;

	public ConsultaService(ConsultaRepositoryPort consultaRepository, PacienteRepositoryPort pacienteRepository,
			UnidadeSaudeRepositoryPort unidadeRepository, ListaEsperaRepositoryPort listaEsperaRepository,
			HistoricoListaEsperaRepositoryPort historicoRepository) {
		this.consultaRepository = consultaRepository;
		this.pacienteRepository = pacienteRepository;
		this.unidadeRepository = unidadeRepository;
		this.listaEsperaRepository = listaEsperaRepository;
		this.historicoRepository = historicoRepository;
	}

	@Transactional
	public Consulta agendar(Consulta consulta) {
		validarReferencias(consulta.pacienteId(), consulta.unidadeSaudeId());
		return consultaRepository.save(new Consulta(null, consulta.pacienteId(), consulta.unidadeSaudeId(),
				consulta.dataHoraConsulta(), consulta.especialidade(), StatusConsulta.AGENDADA));
	}

	@Transactional
	public Consulta confirmar(Long id) {
		return consultaRepository.save(buscarPorId(id).comStatus(StatusConsulta.CONFIRMADA));
	}

	@Transactional
	public Consulta cancelar(Long id) {
		Consulta cancelada = consultaRepository.save(buscarPorId(id).comStatus(StatusConsulta.CANCELADA));
		reagendarProximoDaLista(cancelada);
		return cancelada;
	}

	@Transactional
	public Consulta registrarFalta(Long id) {
		return consultaRepository.save(buscarPorId(id).comStatus(StatusConsulta.FALTOU));
	}

	@Transactional
	public Consulta registrarComparecimento(Long id) {
		return consultaRepository.save(buscarPorId(id).comStatus(StatusConsulta.REALIZADA));
	}

	private Consulta buscarPorId(Long id) {
		return consultaRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Consulta nao encontrada"));
	}

	private void validarReferencias(Long pacienteId, Long unidadeSaudeId) {
		pacienteRepository.findById(pacienteId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Paciente nao encontrado"));
		unidadeRepository.findById(unidadeSaudeId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Unidade de saude nao encontrada"));
	}

	private void reagendarProximoDaLista(Consulta consultaCancelada) {
		listaEsperaRepository.findNextByEspecialidade(consultaCancelada.especialidade()).ifPresent(proximo -> {
			Consulta reagendada = consultaRepository.save(new Consulta(null, proximo.pacienteId(),
					consultaCancelada.unidadeSaudeId(), consultaCancelada.dataHoraConsulta(),
					consultaCancelada.especialidade(), StatusConsulta.AGENDADA));
			historicoRepository.save(new HistoricoListaEspera(null, proximo.pacienteId(), reagendada.id(),
					proximo.especialidade(), "Consulta reagendada automaticamente apos cancelamento",
					OffsetDateTime.now()));
			listaEsperaRepository.deleteById(proximo.id());
		});
	}
}
