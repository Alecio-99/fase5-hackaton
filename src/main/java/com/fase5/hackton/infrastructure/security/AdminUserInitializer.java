package com.fase5.hackton.infrastructure.security;

import com.fase5.hackton.domain.model.Perfil;
import com.fase5.hackton.infrastructure.persistence.entity.UsuarioEntity;
import com.fase5.hackton.infrastructure.persistence.repository.SpringUsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {
	private final SpringUsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final String adminEmail;
	private final String adminPassword;

	public AdminUserInitializer(SpringUsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
			@Value("${APP_ADMIN_EMAIL:admin@sus.local}") String adminEmail,
			@Value("${APP_ADMIN_PASSWORD:admin123}") String adminPassword) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.adminEmail = adminEmail;
		this.adminPassword = adminPassword;
	}

	@Override
	public void run(String... args) {
		if (usuarioRepository.count() == 0) {
			UsuarioEntity admin = new UsuarioEntity();
			admin.setNome("Administrador");
			admin.setEmail(adminEmail);
			admin.setSenhaHash(passwordEncoder.encode(adminPassword));
			admin.setPerfil(Perfil.ADMIN);
			usuarioRepository.save(admin);
		}
	}
}
