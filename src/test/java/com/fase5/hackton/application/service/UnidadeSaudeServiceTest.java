package com.fase5.hackton.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fase5.hackton.application.port.UnidadeSaudeRepositoryPort;
import com.fase5.hackton.domain.exception.RecursoNaoEncontradoException;
import com.fase5.hackton.domain.model.TipoUnidade;
import com.fase5.hackton.domain.model.UnidadeSaude;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UnidadeSaudeServiceTest {

	@Mock
	private UnidadeSaudeRepositoryPort unidadeRepository;

	@InjectMocks
	private UnidadeSaudeService unidadeService;

	@Test
	void criarDevePersistirNovaUnidadeSemId() {
		when(unidadeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		UnidadeSaude nova = new UnidadeSaude(null, "Hospital Central", TipoUnidade.HOSPITAL, "Av X", "1133", 200);
		UnidadeSaude criada = unidadeService.criar(nova);

		assertThat(criada.id()).isNull();
		assertThat(criada.nome()).isEqualTo("Hospital Central");
		assertThat(criada.tipo()).isEqualTo(TipoUnidade.HOSPITAL);
	}

	@Test
	void buscarPorIdInexistenteDeveFalhar() {
		when(unidadeRepository.findById(404L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> unidadeService.buscarPorId(404L))
				.isInstanceOf(RecursoNaoEncontradoException.class);
	}

	@Test
	void atualizarDevePreservarId() {
		UnidadeSaude atual = new UnidadeSaude(3L, "UBS", TipoUnidade.UBS, "Rua", "1", 30);
		when(unidadeRepository.findById(3L)).thenReturn(Optional.of(atual));
		when(unidadeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		UnidadeSaude dados = new UnidadeSaude(null, "UBS Atualizada", TipoUnidade.UBS, "Rua Nova", "2", 40);
		UnidadeSaude atualizada = unidadeService.atualizar(3L, dados);

		assertThat(atualizada.id()).isEqualTo(3L);
		assertThat(atualizada.nome()).isEqualTo("UBS Atualizada");
		assertThat(atualizada.capacidadeAtendimento()).isEqualTo(40);
	}

	@Test
	void excluirInexistenteDeveFalhar() {
		when(unidadeRepository.findById(404L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> unidadeService.excluir(404L))
				.isInstanceOf(RecursoNaoEncontradoException.class);
		verify(unidadeRepository, never()).deleteById(any());
	}

	@Test
	void excluirDeveRemoverQuandoExiste() {
		when(unidadeRepository.findById(3L)).thenReturn(Optional.of(
				new UnidadeSaude(3L, "UBS", TipoUnidade.UBS, "Rua", "1", 30)));

		unidadeService.excluir(3L);

		verify(unidadeRepository).deleteById(3L);
	}
}
