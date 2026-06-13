package com.fase5.hackton.infrastructure.security;

import com.fase5.hackton.infrastructure.persistence.repository.SpringUsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {
	private final SpringUsuarioRepository usuarioRepository;

	public UsuarioDetailsService(SpringUsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		return usuarioRepository.findByEmail(username)
				.map(usuario -> new User(usuario.getEmail(), usuario.getSenhaHash(),
						java.util.List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name()))))
				.orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado"));
	}
}
