package com.fase5.hackton.presentation.resource;

import com.fase5.hackton.application.service.AuthService;
import com.fase5.hackton.domain.model.Perfil;
import com.fase5.hackton.presentation.api.AuthApi;
import com.fase5.hackton.presentation.api.model.AuthResponse;
import com.fase5.hackton.presentation.api.model.LoginRequest;
import com.fase5.hackton.presentation.api.model.RegisterRequest;
import com.fase5.hackton.presentation.mapper.PresentationMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthResource implements AuthApi {
	private final AuthService authService;

	public AuthResource(AuthService authService) {
		this.authService = authService;
	}

	@Override
	public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
		return ResponseEntity.ok(PresentationMapper.toResponse(
				authService.login(loginRequest.getEmail(), loginRequest.getSenha())));
	}

	@Override
	public ResponseEntity<AuthResponse> register(RegisterRequest registerRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(PresentationMapper.toResponse(
				authService.registrar(registerRequest.getNome(), registerRequest.getEmail(), registerRequest.getSenha(),
						Perfil.valueOf(registerRequest.getPerfil().name()))));
	}
}
