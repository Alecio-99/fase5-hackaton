package com.fase5.hackton.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fase5.hackton.application.port.PacienteRepositoryPort;
import com.fase5.hackton.domain.exception.RecursoNaoEncontradoException;
import com.fase5.hackton.domain.exception.RegraNegocioException;
import com.fase5.hackton.domain.model.Paciente;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

	@Mock
	private PacienteRepositoryPort pacienteRepository;

	@InjectMocks
	private PacienteService pacienteService;

	private Paciente novo() {
		return new Paciente(null, "Maria", "12345678901", LocalDate.of(1995, 3, 10), "1199", "m@x.com", "Rua C", "777", null);
	}

	@Test
	void criarDevePersistirQuandoCpfECartaoSaoUnicos() {
		when(pacienteRepository.findByCpf("12345678901")).thenReturn(Optional.empty());
		when(pacienteRepository.existsByCartaoSus("777")).thenReturn(false);
		when(pacienteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		Paciente criado = pacienteService.criar(novo());

		assertThat(criado.nome()).isEqualTo("Maria");
		assertThat(criado.dataCadastro()).isNotNull();
	}

	@Test
	void criarDeveFalharQuandoCpfJaCadastrado() {
		Paciente existente = new Paciente(50L, "Outro", "12345678901", LocalDate.now(), "1", "o@x.com", "Rua", "1", OffsetDateTime.now());
		when(pacienteRepository.findByCpf("12345678901")).thenReturn(Optional.of(existente));

		assertThatThrownBy(() -> pacienteService.criar(novo()))
				.isInstanceOf(RegraNegocioException.class)
				.hasMessageContaining("CPF");
		verify(pacienteRepository, never()).save(any());
	}

	@Test
	void criarDeveFalharQuandoCartaoSusJaCadastrado() {
		when(pacienteRepository.findByCpf("12345678901")).thenReturn(Optional.empty());
		when(pacienteRepository.existsByCartaoSus("777")).thenReturn(true);

		assertThatThrownBy(() -> pacienteService.criar(novo()))
				.isInstanceOf(RegraNegocioException.class)
				.hasMessageContaining("Cartao SUS");
	}

	@Test
	void buscarPorIdInexistenteDeveFalhar() {
		when(pacienteRepository.findById(404L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> pacienteService.buscarPorId(404L))
				.isInstanceOf(RecursoNaoEncontradoException.class);
	}

	@Test
	void buscarPorCpfInexistenteDeveFalhar() {
		when(pacienteRepository.findByCpf("000")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> pacienteService.buscarPorCpf("000"))
				.isInstanceOf(RecursoNaoEncontradoException.class);
	}

	@Test
	void atualizarDevePreservarIdEDataCadastroOriginal() {
		OffsetDateTime cadastro = OffsetDateTime.now().minusDays(10);
		Paciente atual = new Paciente(1L, "Maria", "12345678901", LocalDate.of(1995, 3, 10), "1199", "m@x.com", "Rua C", "777", cadastro);
		when(pacienteRepository.findById(1L)).thenReturn(Optional.of(atual));
		when(pacienteRepository.findByCpf("12345678901")).thenReturn(Optional.of(atual));
		when(pacienteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		Paciente dados = new Paciente(null, "Maria Silva", "12345678901", LocalDate.of(1995, 3, 10), "1198", "m@x.com", "Rua D", "777", null);
		Paciente atualizado = pacienteService.atualizar(1L, dados);

		assertThat(atualizado.id()).isEqualTo(1L);
		assertThat(atualizado.nome()).isEqualTo("Maria Silva");
		assertThat(atualizado.dataCadastro()).isEqualTo(cadastro);
	}

	@Test
	void excluirDeveRemoverQuandoExiste() {
		Paciente atual = new Paciente(1L, "Maria", "1", LocalDate.now(), "1", "m@x.com", "Rua", "1", OffsetDateTime.now());
		when(pacienteRepository.findById(1L)).thenReturn(Optional.of(atual));

		pacienteService.excluir(1L);

		verify(pacienteRepository).deleteById(1L);
	}

	@Test
	void excluirInexistenteDeveFalhar() {
		when(pacienteRepository.findById(404L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> pacienteService.excluir(404L))
				.isInstanceOf(RecursoNaoEncontradoException.class);
		verify(pacienteRepository, never()).deleteById(any());
	}
}
