package com.fase5.hackton.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fase5.hackton.application.port.UsuarioRepositoryPort;
import com.fase5.hackton.domain.exception.CredenciaisInvalidasException;
import com.fase5.hackton.domain.exception.RegraNegocioException;
import com.fase5.hackton.domain.model.Perfil;
import com.fase5.hackton.domain.model.Usuario;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UsuarioRepositoryPort usuarioRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private TokenServicePort tokenService;

	@InjectMocks
	private AuthService authService;

	@Test
	void registrarDeveCriarUsuarioEGerarToken() {
		when(usuarioRepository.existsByEmail("novo@x.com")).thenReturn(false);
		when(passwordEncoder.encode("senha123")).thenReturn("HASH");
		when(usuarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
		when(tokenService.gerarToken(any())).thenReturn("jwt-token");

		AuthResult resultado = authService.registrar("Novo", "novo@x.com", "senha123", Perfil.RECEPCAO);

		assertThat(resultado.token()).isEqualTo("jwt-token");
		assertThat(resultado.usuario().senhaHash()).isEqualTo("HASH");
		assertThat(resultado.usuario().perfil()).isEqualTo(Perfil.RECEPCAO);
	}

	@Test
	void registrarDeveFalharQuandoEmailJaExiste() {
		when(usuarioRepository.existsByEmail("existe@x.com")).thenReturn(true);

		assertThatThrownBy(() -> authService.registrar("X", "existe@x.com", "senha123", Perfil.ADMIN))
				.isInstanceOf(RegraNegocioException.class);
		verify(usuarioRepository, never()).save(any());
	}

	@Test
	void loginDeveAutenticarComCredenciaisValidas() {
		Usuario usuario = new Usuario(1L, "Admin", "admin@x.com", "HASH", Perfil.ADMIN);
		when(usuarioRepository.findByEmail("admin@x.com")).thenReturn(Optional.of(usuario));
		when(passwordEncoder.matches("senha123", "HASH")).thenReturn(true);
		when(tokenService.gerarToken(usuario)).thenReturn("jwt-token");

		AuthResult resultado = authService.login("admin@x.com", "senha123");

		assertThat(resultado.token()).isEqualTo("jwt-token");
		assertThat(resultado.usuario()).isEqualTo(usuario);
	}

	@Test
	void loginDeveFalharQuandoEmailNaoExiste() {
		when(usuarioRepository.findByEmail("nao@x.com")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> authService.login("nao@x.com", "senha123"))
				.isInstanceOf(CredenciaisInvalidasException.class);
	}

	@Test
	void loginDeveFalharQuandoSenhaIncorreta() {
		Usuario usuario = new Usuario(1L, "Admin", "admin@x.com", "HASH", Perfil.ADMIN);
		when(usuarioRepository.findByEmail("admin@x.com")).thenReturn(Optional.of(usuario));
		when(passwordEncoder.matches("errada", "HASH")).thenReturn(false);

		assertThatThrownBy(() -> authService.login("admin@x.com", "errada"))
				.isInstanceOf(CredenciaisInvalidasException.class);
	}
}
