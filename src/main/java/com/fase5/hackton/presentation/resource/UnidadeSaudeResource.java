package com.fase5.hackton.presentation.resource;

import com.fase5.hackton.application.service.UnidadeSaudeService;
import com.fase5.hackton.presentation.api.UnidadesSaudeApi;
import com.fase5.hackton.presentation.api.model.UnidadeSaudeRequest;
import com.fase5.hackton.presentation.api.model.UnidadeSaudeResponse;
import com.fase5.hackton.presentation.mapper.PresentationMapper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnidadeSaudeResource implements UnidadesSaudeApi {
	private final UnidadeSaudeService unidadeSaudeService;

	public UnidadeSaudeResource(UnidadeSaudeService unidadeSaudeService) {
		this.unidadeSaudeService = unidadeSaudeService;
	}

	@Override
	public ResponseEntity<UnidadeSaudeResponse> atualizarUnidade(Long id, UnidadeSaudeRequest unidadeSaudeRequest) {
		return ResponseEntity.ok(PresentationMapper.toResponse(
				unidadeSaudeService.atualizar(id, PresentationMapper.toDomain(unidadeSaudeRequest))));
	}

	@Override
	public ResponseEntity<UnidadeSaudeResponse> buscarUnidadePorId(Long id) {
		return ResponseEntity.ok(PresentationMapper.toResponse(unidadeSaudeService.buscarPorId(id)));
	}

	@Override
	public ResponseEntity<UnidadeSaudeResponse> criarUnidade(UnidadeSaudeRequest unidadeSaudeRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(PresentationMapper.toResponse(unidadeSaudeService.criar(PresentationMapper.toDomain(unidadeSaudeRequest))));
	}

	@Override
	public ResponseEntity<Void> excluirUnidade(Long id) {
		unidadeSaudeService.excluir(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<List<UnidadeSaudeResponse>> listarUnidades() {
		return ResponseEntity.ok(unidadeSaudeService.listar().stream().map(PresentationMapper::toResponse).toList());
	}
}
