package com.fase5.hackton.application.service;

import com.fase5.hackton.application.port.UsuarioRepositoryPort;
import com.fase5.hackton.domain.exception.CredenciaisInvalidasException;
import com.fase5.hackton.domain.exception.RegraNegocioException;
import com.fase5.hackton.domain.model.Perfil;
import com.fase5.hackton.domain.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
	private final UsuarioRepositoryPort usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenServicePort tokenService;

	public AuthService(UsuarioRepositoryPort usuarioRepository, PasswordEncoder passwordEncoder,
			TokenServicePort tokenService) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenService = tokenService;
	}

	@Transactional
	public AuthResult registrar(String nome, String email, String senha, Perfil perfil) {
		if (usuarioRepository.existsByEmail(email)) {
			throw new RegraNegocioException("E-mail ja cadastrado");
		}
		Usuario usuario = usuarioRepository.save(new Usuario(null, nome, email, passwordEncoder.encode(senha), perfil));
		return new AuthResult(tokenService.gerarToken(usuario), usuario);
	}

	@Transactional(readOnly = true)
	public AuthResult login(String email, String senha) {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(CredenciaisInvalidasException::new);
		if (!passwordEncoder.matches(senha, usuario.senhaHash())) {
			throw new CredenciaisInvalidasException();
		}
		return new AuthResult(tokenService.gerarToken(usuario), usuario);
	}
}
