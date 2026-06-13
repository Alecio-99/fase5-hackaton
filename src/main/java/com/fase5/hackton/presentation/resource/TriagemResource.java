package com.fase5.hackton.presentation.resource;

import com.fase5.hackton.application.service.TriagemService;
import com.fase5.hackton.presentation.api.TriagensApi;
import com.fase5.hackton.presentation.api.model.TriagemRequest;
import com.fase5.hackton.presentation.api.model.TriagemResponse;
import com.fase5.hackton.presentation.mapper.PresentationMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TriagemResource implements TriagensApi {
	private final TriagemService triagemService;

	public TriagemResource(TriagemService triagemService) {
		this.triagemService = triagemService;
	}

	@Override
	public ResponseEntity<TriagemResponse> buscarTriagemPorId(Long id) {
		return ResponseEntity.ok(PresentationMapper.toResponse(triagemService.buscarPorId(id)));
	}

	@Override
	public ResponseEntity<TriagemResponse> criarTriagem(TriagemRequest triagemRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(PresentationMapper.toResponse(triagemService.criar(PresentationMapper.toDomain(triagemRequest))));
	}
}
