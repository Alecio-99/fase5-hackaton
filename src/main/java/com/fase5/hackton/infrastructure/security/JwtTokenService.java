package com.fase5.hackton.infrastructure.security;

import com.fase5.hackton.application.service.TokenServicePort;
import com.fase5.hackton.domain.model.Perfil;
import com.fase5.hackton.domain.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService implements TokenServicePort {
	private final SecretKey secretKey;
	private final long expirationMinutes;

	public JwtTokenService(@Value("${app.jwt.secret}") String secret,
			@Value("${app.jwt.expiration-minutes}") long expirationMinutes) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationMinutes = expirationMinutes;
	}

	@Override
	public String gerarToken(Usuario usuario) {
		Instant now = Instant.now();
		return Jwts.builder()
				.subject(usuario.email())
				.claim("perfil", usuario.perfil().name())
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plusSeconds(expirationMinutes * 60)))
				.signWith(secretKey)
				.compact();
	}

	public String obterEmail(String token) {
		return claims(token).getSubject();
	}

	public Perfil obterPerfil(String token) {
		return Perfil.valueOf(claims(token).get("perfil", String.class));
	}

	public boolean tokenValido(String token) {
		try {
			claims(token);
			return true;
		} catch (RuntimeException ex) {
			return false;
		}
	}

	private Claims claims(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}
