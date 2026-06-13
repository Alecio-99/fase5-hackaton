package com.fase5.hackton.presentation.resource;

import com.fase5.hackton.application.service.PacienteService;
import com.fase5.hackton.presentation.api.PacientesApi;
import com.fase5.hackton.presentation.api.model.PacienteRequest;
import com.fase5.hackton.presentation.api.model.PacienteResponse;
import com.fase5.hackton.presentation.mapper.PresentationMapper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PacienteResource implements PacientesApi {
	private final PacienteService pacienteService;

	public PacienteResource(PacienteService pacienteService) {
		this.pacienteService = pacienteService;
	}

	@Override
	public ResponseEntity<PacienteResponse> atualizarPaciente(Long id, PacienteRequest pacienteRequest) {
		return ResponseEntity.ok(PresentationMapper.toResponse(
				pacienteService.atualizar(id, PresentationMapper.toDomain(pacienteRequest))));
	}

	@Override
	public ResponseEntity<PacienteResponse> buscarPacientePorCpf(String cpf) {
		return ResponseEntity.ok(PresentationMapper.toResponse(pacienteService.buscarPorCpf(cpf)));
	}

	@Override
	public ResponseEntity<PacienteResponse> buscarPacientePorId(Long id) {
		return ResponseEntity.ok(PresentationMapper.toResponse(pacienteService.buscarPorId(id)));
	}

	@Override
	public ResponseEntity<PacienteResponse> criarPaciente(PacienteRequest pacienteRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(PresentationMapper.toResponse(pacienteService.criar(PresentationMapper.toDomain(pacienteRequest))));
	}

	@Override
	public ResponseEntity<Void> excluirPaciente(Long id) {
		pacienteService.excluir(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<List<PacienteResponse>> listarPacientes() {
		return ResponseEntity.ok(pacienteService.listar().stream().map(PresentationMapper::toResponse).toList());
	}
}
