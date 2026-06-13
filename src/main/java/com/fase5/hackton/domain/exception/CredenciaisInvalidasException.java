package com.fase5.hackton.domain.exception;

public class CredenciaisInvalidasException extends RuntimeException {
	public CredenciaisInvalidasException() {
		super("Credenciais invalidas");
	}
}
