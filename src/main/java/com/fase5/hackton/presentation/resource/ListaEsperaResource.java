package com.fase5.hackton.presentation.resource;

import com.fase5.hackton.application.service.ListaEsperaService;
import com.fase5.hackton.presentation.api.ListaEsperaApi;
import com.fase5.hackton.presentation.api.model.ListaEsperaRequest;
import com.fase5.hackton.presentation.api.model.ListaEsperaResponse;
import com.fase5.hackton.presentation.mapper.PresentationMapper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ListaEsperaResource implements ListaEsperaApi {
	private final ListaEsperaService listaEsperaService;

	public ListaEsperaResource(ListaEsperaService listaEsperaService) {
		this.listaEsperaService = listaEsperaService;
	}

	@Override
	public ResponseEntity<ListaEsperaResponse> cadastrarNaListaEspera(ListaEsperaRequest listaEsperaRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(PresentationMapper.toResponse(listaEsperaService.cadastrar(PresentationMapper.toDomain(listaEsperaRequest))));
	}

	@Override
	public ResponseEntity<List<ListaEsperaResponse>> listarEspera() {
		return ResponseEntity.ok(listaEsperaService.listar().stream().map(PresentationMapper::toResponse).toList());
	}
}
