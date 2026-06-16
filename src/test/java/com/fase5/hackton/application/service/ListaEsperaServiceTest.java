package com.fase5.hackton.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fase5.hackton.application.port.ListaEsperaRepositoryPort;
import com.fase5.hackton.application.port.PacienteRepositoryPort;
import com.fase5.hackton.domain.exception.RecursoNaoEncontradoException;
import com.fase5.hackton.domain.model.ListaEspera;
import com.fase5.hackton.domain.model.Paciente;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListaEsperaServiceTest {

	@Mock
	private ListaEsperaRepositoryPort listaEsperaRepository;
	@Mock
	private PacienteRepositoryPort pacienteRepository;

	@InjectMocks
	private ListaEsperaService listaEsperaService;

	@Test
	void cadastrarDevePersistirComDataDeCadastroQuandoPacienteExiste() {
		when(pacienteRepository.findById(1L)).thenReturn(Optional.of(
				new Paciente(1L, "Joao", "1", LocalDate.now(), "1", "j@x.com", "Rua", "1", OffsetDateTime.now())));
		when(listaEsperaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		ListaEspera entrada = new ListaEspera(null, 1L, "ORTOPEDIA", 2, null);
		ListaEspera salva = listaEsperaService.cadastrar(entrada);

		assertThat(salva.especialidade()).isEqualTo("ORTOPEDIA");
		assertThat(salva.prioridade()).isEqualTo(2);
		assertThat(salva.dataCadastro()).isNotNull();
	}

	@Test
	void cadastrarDeveFalharQuandoPacienteNaoExiste() {
		when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

		ListaEspera entrada = new ListaEspera(null, 99L, "ORTOPEDIA", 2, null);
		assertThatThrownBy(() -> listaEsperaService.cadastrar(entrada))
				.isInstanceOf(RecursoNaoEncontradoException.class);
		verify(listaEsperaRepository, never()).save(any());
	}

	@Test
	void listarDeveDelegarAoRepositorio() {
		ListaEspera item = new ListaEspera(1L, 1L, "ORTOPEDIA", 2, OffsetDateTime.now());
		when(listaEsperaRepository.findAll()).thenReturn(List.of(item));

		assertThat(listaEsperaService.listar()).containsExactly(item);
	}
}
