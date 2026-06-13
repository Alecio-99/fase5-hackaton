package com.fase5.hackton.presentation.resource;

import com.fase5.hackton.application.service.ConsultaService;
import com.fase5.hackton.presentation.api.ConsultasApi;
import com.fase5.hackton.presentation.api.model.ConsultaRequest;
import com.fase5.hackton.presentation.api.model.ConsultaResponse;
import com.fase5.hackton.presentation.mapper.PresentationMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsultaResource implements ConsultasApi {
	private final ConsultaService consultaService;

	public ConsultaResource(ConsultaService consultaService) {
		this.consultaService = consultaService;
	}

	@Override
	public ResponseEntity<ConsultaResponse> agendarConsulta(ConsultaRequest consultaRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(PresentationMapper.toResponse(consultaService.agendar(PresentationMapper.toDomain(consultaRequest))));
	}

	@Override
	public ResponseEntity<ConsultaResponse> cancelarConsulta(Long id) {
		return ResponseEntity.ok(PresentationMapper.toResponse(consultaService.cancelar(id)));
	}

	@Override
	public ResponseEntity<ConsultaResponse> confirmarConsulta(Long id) {
		return ResponseEntity.ok(PresentationMapper.toResponse(consultaService.confirmar(id)));
	}

	@Override
	public ResponseEntity<ConsultaResponse> registrarComparecimento(Long id) {
		return ResponseEntity.ok(PresentationMapper.toResponse(consultaService.registrarComparecimento(id)));
	}

	@Override
	public ResponseEntity<ConsultaResponse> registrarFalta(Long id) {
		return ResponseEntity.ok(PresentationMapper.toResponse(consultaService.registrarFalta(id)));
	}
}
