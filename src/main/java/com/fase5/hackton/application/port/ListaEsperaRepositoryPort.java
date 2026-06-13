package com.fase5.hackton.application.port;

import com.fase5.hackton.domain.model.ListaEspera;
import java.util.List;
import java.util.Optional;

public interface ListaEsperaRepositoryPort {
	ListaEspera save(ListaEspera listaEspera);
	List<ListaEspera> findAll();
	Optional<ListaEspera> findNextByEspecialidade(String especialidade);
	void deleteById(Long id);
}
