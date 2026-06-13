package com.fase5.hackton.domain.model;

public record RespostasTriagem(
		boolean possuiFebre,
		boolean possuiFaltaDeAr,
		boolean possuiDorIntensa,
		boolean possuiSangramento,
		boolean possuiTontura
) {
}
