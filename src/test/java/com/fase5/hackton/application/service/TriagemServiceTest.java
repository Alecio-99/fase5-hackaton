package com.fase5.hackton.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fase5.hackton.application.port.PacienteRepositoryPort;
import com.fase5.hackton.application.port.TriagemRepositoryPort;
import com.fase5.hackton.domain.exception.RecursoNaoEncontradoException;
import com.fase5.hackton.domain.model.ClassificacaoRisco;
import com.fase5.hackton.domain.model.Paciente;
import com.fase5.hackton.domain.model.RespostasTriagem;
import com.fase5.hackton.domain.model.Triagem;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TriagemServiceTest {

	@Mock
	private TriagemRepositoryPort triagemRepository;
	@Mock
	private PacienteRepositoryPort pacienteRepository;

	@InjectMocks
	private TriagemService triagemService;

	@Test
	void criarDeveClassificarRiscoAutomaticamenteAoSalvar() {
		when(pacienteRepository.findById(1L)).thenReturn(Optional.of(
				new Paciente(1L, "Joao", "1", LocalDate.now(), "1", "j@x.com", "Rua", "1", OffsetDateTime.now())));
		when(triagemRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		// falta de ar -> VERMELHO
		Triagem entrada = new Triagem(null, 1L, null,
				new RespostasTriagem(false, true, false, false, false), null, "obs");
		Triagem salva = triagemService.criar(entrada);

		assertThat(salva.classificacaoRisco()).isEqualTo(ClassificacaoRisco.VERMELHO);
		assertThat(salva.dataTriagem()).isNotNull();
	}

	@Test
	void criarDeveFalharQuandoPacienteNaoExiste() {
		when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

		Triagem entrada = new Triagem(null, 99L, null,
				new RespostasTriagem(false, false, false, false, false), null, null);
		assertThatThrownBy(() -> triagemService.criar(entrada))
				.isInstanceOf(RecursoNaoEncontradoException.class);
		verify(triagemRepository, never()).save(any());
	}

	@Test
	void buscarPorIdInexistenteDeveFalhar() {
		when(triagemRepository.findById(404L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> triagemService.buscarPorId(404L))
				.isInstanceOf(RecursoNaoEncontradoException.class);
	}
}
