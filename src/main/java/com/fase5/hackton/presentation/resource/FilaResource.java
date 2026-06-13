package com.fase5.hackton.presentation.resource;

import com.fase5.hackton.application.service.FilaService;
import com.fase5.hackton.presentation.api.FilaApi;
import com.fase5.hackton.presentation.api.model.FilaEntradaRequest;
import com.fase5.hackton.presentation.api.model.FilaPosicaoResponse;
import com.fase5.hackton.presentation.api.model.FilaResponse;
import com.fase5.hackton.presentation.mapper.PresentationMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilaResource implements FilaApi {
	private final FilaService filaService;

	public FilaResource(FilaService filaService) {
		this.filaService = filaService;
	}

	@Override
	public ResponseEntity<FilaResponse> cancelarAtendimento(Long id) {
		return ResponseEntity.ok(PresentationMapper.toResponse(filaService.cancelar(id)));
	}

	@Override
	public ResponseEntity<FilaResponse> chamarProximoPaciente(Long unidadeId) {
		return ResponseEntity.ok(PresentationMapper.toResponse(filaService.chamarProximo(unidadeId)));
	}

	@Override
	public ResponseEntity<FilaPosicaoResponse> consultarPosicao(Long id) {
		return ResponseEntity.ok(PresentationMapper.filaPosicao(id, filaService.consultarPosicao(id)));
	}

	@Override
	public ResponseEntity<FilaResponse> entrarNaFila(FilaEntradaRequest filaEntradaRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(PresentationMapper.toResponse(
				filaService.entrarNaFila(filaEntradaRequest.getPacienteId(), filaEntradaRequest.getUnidadeSaudeId(),
						filaEntradaRequest.getTriagemId())));
	}

	@Override
	public ResponseEntity<FilaResponse> finalizarAtendimento(Long id) {
		return ResponseEntity.ok(PresentationMapper.toResponse(filaService.finalizar(id)));
	}
}
