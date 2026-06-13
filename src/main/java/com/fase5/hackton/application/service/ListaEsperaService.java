package com.fase5.hackton.application.service;

import com.fase5.hackton.application.port.ListaEsperaRepositoryPort;
import com.fase5.hackton.application.port.PacienteRepositoryPort;
import com.fase5.hackton.domain.exception.RecursoNaoEncontradoException;
import com.fase5.hackton.domain.model.ListaEspera;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListaEsperaService {
	private final ListaEsperaRepositoryPort listaEsperaRepository;
	private final PacienteRepositoryPort pacienteRepository;

	public ListaEsperaService(ListaEsperaRepositoryPort listaEsperaRepository, PacienteRepositoryPort pacienteRepository) {
		this.listaEsperaRepository = listaEsperaRepository;
		this.pacienteRepository = pacienteRepository;
	}

	@Transactional
	public ListaEspera cadastrar(ListaEspera listaEspera) {
		pacienteRepository.findById(listaEspera.pacienteId())
				.orElseThrow(() -> new RecursoNaoEncontradoException("Paciente nao encontrado"));
		return listaEsperaRepository.save(new ListaEspera(null, listaEspera.pacienteId(), listaEspera.especialidade(),
				listaEspera.prioridade(), OffsetDateTime.now()));
	}

	@Transactional(readOnly = true)
	public List<ListaEspera> listar() {
		return listaEsperaRepository.findAll();
	}
}
