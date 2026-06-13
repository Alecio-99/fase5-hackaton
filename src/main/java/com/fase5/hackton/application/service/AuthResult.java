package com.fase5.hackton.application.service;

import com.fase5.hackton.domain.model.Usuario;

public record AuthResult(String token, Usuario usuario) {
}
