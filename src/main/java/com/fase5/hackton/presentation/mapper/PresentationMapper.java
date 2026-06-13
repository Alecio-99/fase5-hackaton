package com.fase5.hackton.presentation.mapper;

import com.fase5.hackton.application.service.AuthResult;
import com.fase5.hackton.domain.model.Consulta;
import com.fase5.hackton.domain.model.DashboardStats;
import com.fase5.hackton.domain.model.FilaAtendimento;
import com.fase5.hackton.domain.model.ListaEspera;
import com.fase5.hackton.domain.model.Paciente;
import com.fase5.hackton.domain.model.Triagem;
import com.fase5.hackton.domain.model.UnidadeSaude;
import com.fase5.hackton.domain.model.Usuario;
import com.fase5.hackton.presentation.api.model.AuthResponse;
import com.fase5.hackton.presentation.api.model.ConsultaRequest;
import com.fase5.hackton.presentation.api.model.ConsultaResponse;
import com.fase5.hackton.presentation.api.model.DashboardResponse;
import com.fase5.hackton.presentation.api.model.FilaEntradaRequest;
import com.fase5.hackton.presentation.api.model.FilaPosicaoResponse;
import com.fase5.hackton.presentation.api.model.FilaResponse;
import com.fase5.hackton.presentation.api.model.ListaEsperaRequest;
import com.fase5.hackton.presentation.api.model.ListaEsperaResponse;
import com.fase5.hackton.presentation.api.model.PacienteRequest;
import com.fase5.hackton.presentation.api.model.PacienteResponse;
import com.fase5.hackton.presentation.api.model.TriagemRequest;
import com.fase5.hackton.presentation.api.model.TriagemResponse;
import com.fase5.hackton.presentation.api.model.UnidadeSaudeRequest;
import com.fase5.hackton.presentation.api.model.UnidadeSaudeResponse;
import com.fase5.hackton.presentation.api.model.UsuarioResponse;
import java.util.Map;
import java.util.stream.Collectors;

public final class PresentationMapper {
	private PresentationMapper() {
	}

	public static Paciente toDomain(PacienteRequest request) {
		return new Paciente(null, request.getNome(), request.getCpf(), request.getDataNascimento(), request.getTelefone(),
				request.getEmail(), request.getEndereco(), request.getCartaoSus(), null);
	}

	public static PacienteResponse toResponse(Paciente paciente) {
		return new PacienteResponse()
				.id(paciente.id())
				.nome(paciente.nome())
				.cpf(paciente.cpf())
				.dataNascimento(paciente.dataNascimento())
				.telefone(paciente.telefone())
				.email(paciente.email())
				.endereco(paciente.endereco())
				.cartaoSus(paciente.cartaoSus())
				.dataCadastro(paciente.dataCadastro());
	}

	public static UnidadeSaude toDomain(UnidadeSaudeRequest request) {
		return new UnidadeSaude(null, request.getNome(), com.fase5.hackton.domain.model.TipoUnidade.valueOf(request.getTipo().name()),
				request.getEndereco(), request.getTelefone(), request.getCapacidadeAtendimento());
	}

	public static UnidadeSaudeResponse toResponse(UnidadeSaude unidade) {
		return new UnidadeSaudeResponse()
				.id(unidade.id())
				.nome(unidade.nome())
				.tipo(com.fase5.hackton.presentation.api.model.TipoUnidade.valueOf(unidade.tipo().name()))
				.endereco(unidade.endereco())
				.telefone(unidade.telefone())
				.capacidadeAtendimento(unidade.capacidadeAtendimento());
	}

	public static Triagem toDomain(TriagemRequest request) {
		return new Triagem(null, request.getPacienteId(), null,
				new com.fase5.hackton.domain.model.RespostasTriagem(
						Boolean.TRUE.equals(request.getRespostas().getPossuiFebre()),
						Boolean.TRUE.equals(request.getRespostas().getPossuiFaltaDeAr()),
						Boolean.TRUE.equals(request.getRespostas().getPossuiDorIntensa()),
						Boolean.TRUE.equals(request.getRespostas().getPossuiSangramento()),
						Boolean.TRUE.equals(request.getRespostas().getPossuiTontura())),
				null, request.getObservacoes());
	}

	public static TriagemResponse toResponse(Triagem triagem) {
		return new TriagemResponse()
				.id(triagem.id())
				.pacienteId(triagem.pacienteId())
				.dataTriagem(triagem.dataTriagem())
				.respostas(new com.fase5.hackton.presentation.api.model.RespostasTriagem()
						.possuiFebre(triagem.respostas().possuiFebre())
						.possuiFaltaDeAr(triagem.respostas().possuiFaltaDeAr())
						.possuiDorIntensa(triagem.respostas().possuiDorIntensa())
						.possuiSangramento(triagem.respostas().possuiSangramento())
						.possuiTontura(triagem.respostas().possuiTontura()))
				.classificacaoRisco(com.fase5.hackton.presentation.api.model.ClassificacaoRisco.valueOf(triagem.classificacaoRisco().name()))
				.observacoes(triagem.observacoes());
	}

	public static FilaResponse toResponse(FilaAtendimento fila) {
		return new FilaResponse()
				.id(fila.id())
				.pacienteId(fila.pacienteId())
				.unidadeSaudeId(fila.unidadeSaudeId())
				.classificacaoRisco(com.fase5.hackton.presentation.api.model.ClassificacaoRisco.valueOf(fila.classificacaoRisco().name()))
				.horarioEntrada(fila.horarioEntrada())
				.status(com.fase5.hackton.presentation.api.model.StatusFila.valueOf(fila.status().name()));
	}

	public static Long pacienteId(FilaEntradaRequest request) {
		return request.getPacienteId();
	}

	public static Consulta toDomain(ConsultaRequest request) {
		return new Consulta(null, request.getPacienteId(), request.getUnidadeSaudeId(), request.getDataHoraConsulta(),
				request.getEspecialidade(), null);
	}

	public static ConsultaResponse toResponse(Consulta consulta) {
		return new ConsultaResponse()
				.id(consulta.id())
				.pacienteId(consulta.pacienteId())
				.unidadeSaudeId(consulta.unidadeSaudeId())
				.dataHoraConsulta(consulta.dataHoraConsulta())
				.especialidade(consulta.especialidade())
				.status(com.fase5.hackton.presentation.api.model.StatusConsulta.valueOf(consulta.status().name()));
	}

	public static ListaEspera toDomain(ListaEsperaRequest request) {
		return new ListaEspera(null, request.getPacienteId(), request.getEspecialidade(), request.getPrioridade(), null);
	}

	public static ListaEsperaResponse toResponse(ListaEspera listaEspera) {
		return new ListaEsperaResponse()
				.id(listaEspera.id())
				.pacienteId(listaEspera.pacienteId())
				.especialidade(listaEspera.especialidade())
				.prioridade(listaEspera.prioridade())
				.dataCadastro(listaEspera.dataCadastro());
	}

	public static FilaPosicaoResponse filaPosicao(Long filaId, int posicao) {
		return new FilaPosicaoResponse().filaId(filaId).posicao(posicao);
	}

	public static DashboardResponse toResponse(DashboardStats stats) {
		Map<String, Long> porRisco = stats.atendimentosPorClassificacaoRisco().entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey().name(), Map.Entry::getValue));
		return new DashboardResponse()
				.quantidadePacientes(stats.quantidadePacientes())
				.consultasAgendadas(stats.consultasAgendadas())
				.consultasRealizadas(stats.consultasRealizadas())
				.consultasPerdidas(stats.consultasPerdidas())
				.taxaAbsenteismo(stats.taxaAbsenteismo())
				.pacientesEmFila(stats.pacientesEmFila())
				.atendimentosPorClassificacaoRisco(porRisco);
	}

	public static AuthResponse toResponse(AuthResult authResult) {
		return new AuthResponse()
				.token(authResult.token())
				.tipo("Bearer")
				.usuario(toResponse(authResult.usuario()));
	}

	public static UsuarioResponse toResponse(Usuario usuario) {
		return new UsuarioResponse()
				.id(usuario.id())
				.nome(usuario.nome())
				.email(usuario.email())
				.perfil(com.fase5.hackton.presentation.api.model.Perfil.valueOf(usuario.perfil().name()));
	}
}
