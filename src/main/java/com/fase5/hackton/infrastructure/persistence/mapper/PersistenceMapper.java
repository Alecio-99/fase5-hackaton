package com.fase5.hackton.infrastructure.persistence.mapper;

import com.fase5.hackton.domain.model.Consulta;
import com.fase5.hackton.domain.model.FilaAtendimento;
import com.fase5.hackton.domain.model.HistoricoListaEspera;
import com.fase5.hackton.domain.model.ListaEspera;
import com.fase5.hackton.domain.model.Paciente;
import com.fase5.hackton.domain.model.RespostasTriagem;
import com.fase5.hackton.domain.model.Triagem;
import com.fase5.hackton.domain.model.UnidadeSaude;
import com.fase5.hackton.domain.model.Usuario;
import com.fase5.hackton.infrastructure.persistence.entity.ConsultaEntity;
import com.fase5.hackton.infrastructure.persistence.entity.FilaAtendimentoEntity;
import com.fase5.hackton.infrastructure.persistence.entity.HistoricoListaEsperaEntity;
import com.fase5.hackton.infrastructure.persistence.entity.ListaEsperaEntity;
import com.fase5.hackton.infrastructure.persistence.entity.PacienteEntity;
import com.fase5.hackton.infrastructure.persistence.entity.TriagemEntity;
import com.fase5.hackton.infrastructure.persistence.entity.UnidadeSaudeEntity;
import com.fase5.hackton.infrastructure.persistence.entity.UsuarioEntity;

public final class PersistenceMapper {
	private PersistenceMapper() {
	}

	public static Paciente toDomain(PacienteEntity entity) {
		return new Paciente(entity.getId(), entity.getNome(), entity.getCpf(), entity.getDataNascimento(),
				entity.getTelefone(), entity.getEmail(), entity.getEndereco(), entity.getCartaoSus(),
				entity.getDataCadastro());
	}

	public static PacienteEntity toEntity(Paciente paciente) {
		PacienteEntity entity = new PacienteEntity();
		entity.setId(paciente.id());
		entity.setNome(paciente.nome());
		entity.setCpf(paciente.cpf());
		entity.setDataNascimento(paciente.dataNascimento());
		entity.setTelefone(paciente.telefone());
		entity.setEmail(paciente.email());
		entity.setEndereco(paciente.endereco());
		entity.setCartaoSus(paciente.cartaoSus());
		entity.setDataCadastro(paciente.dataCadastro());
		return entity;
	}

	public static UnidadeSaude toDomain(UnidadeSaudeEntity entity) {
		return new UnidadeSaude(entity.getId(), entity.getNome(), entity.getTipo(), entity.getEndereco(),
				entity.getTelefone(), entity.getCapacidadeAtendimento());
	}

	public static UnidadeSaudeEntity toEntity(UnidadeSaude unidade) {
		UnidadeSaudeEntity entity = new UnidadeSaudeEntity();
		entity.setId(unidade.id());
		entity.setNome(unidade.nome());
		entity.setTipo(unidade.tipo());
		entity.setEndereco(unidade.endereco());
		entity.setTelefone(unidade.telefone());
		entity.setCapacidadeAtendimento(unidade.capacidadeAtendimento());
		return entity;
	}

	public static Triagem toDomain(TriagemEntity entity) {
		return new Triagem(entity.getId(), entity.getPacienteId(), entity.getDataTriagem(),
				new RespostasTriagem(entity.isPossuiFebre(), entity.isPossuiFaltaDeAr(), entity.isPossuiDorIntensa(),
						entity.isPossuiSangramento(), entity.isPossuiTontura()),
				entity.getClassificacaoRisco(), entity.getObservacoes());
	}

	public static TriagemEntity toEntity(Triagem triagem) {
		TriagemEntity entity = new TriagemEntity();
		entity.setId(triagem.id());
		entity.setPacienteId(triagem.pacienteId());
		entity.setDataTriagem(triagem.dataTriagem());
		entity.setPossuiFebre(triagem.respostas().possuiFebre());
		entity.setPossuiFaltaDeAr(triagem.respostas().possuiFaltaDeAr());
		entity.setPossuiDorIntensa(triagem.respostas().possuiDorIntensa());
		entity.setPossuiSangramento(triagem.respostas().possuiSangramento());
		entity.setPossuiTontura(triagem.respostas().possuiTontura());
		entity.setClassificacaoRisco(triagem.classificacaoRisco());
		entity.setObservacoes(triagem.observacoes());
		return entity;
	}

	public static FilaAtendimento toDomain(FilaAtendimentoEntity entity) {
		return new FilaAtendimento(entity.getId(), entity.getPacienteId(), entity.getUnidadeSaudeId(),
				entity.getClassificacaoRisco(), entity.getHorarioEntrada(), entity.getStatus());
	}

	public static FilaAtendimentoEntity toEntity(FilaAtendimento fila) {
		FilaAtendimentoEntity entity = new FilaAtendimentoEntity();
		entity.setId(fila.id());
		entity.setPacienteId(fila.pacienteId());
		entity.setUnidadeSaudeId(fila.unidadeSaudeId());
		entity.setClassificacaoRisco(fila.classificacaoRisco());
		entity.setHorarioEntrada(fila.horarioEntrada());
		entity.setStatus(fila.status());
		return entity;
	}

	public static Consulta toDomain(ConsultaEntity entity) {
		return new Consulta(entity.getId(), entity.getPacienteId(), entity.getUnidadeSaudeId(),
				entity.getDataHoraConsulta(), entity.getEspecialidade(), entity.getStatus());
	}

	public static ConsultaEntity toEntity(Consulta consulta) {
		ConsultaEntity entity = new ConsultaEntity();
		entity.setId(consulta.id());
		entity.setPacienteId(consulta.pacienteId());
		entity.setUnidadeSaudeId(consulta.unidadeSaudeId());
		entity.setDataHoraConsulta(consulta.dataHoraConsulta());
		entity.setEspecialidade(consulta.especialidade());
		entity.setStatus(consulta.status());
		return entity;
	}

	public static ListaEspera toDomain(ListaEsperaEntity entity) {
		return new ListaEspera(entity.getId(), entity.getPacienteId(), entity.getEspecialidade(),
				entity.getPrioridade(), entity.getDataCadastro());
	}

	public static ListaEsperaEntity toEntity(ListaEspera listaEspera) {
		ListaEsperaEntity entity = new ListaEsperaEntity();
		entity.setId(listaEspera.id());
		entity.setPacienteId(listaEspera.pacienteId());
		entity.setEspecialidade(listaEspera.especialidade());
		entity.setPrioridade(listaEspera.prioridade());
		entity.setDataCadastro(listaEspera.dataCadastro());
		return entity;
	}

	public static HistoricoListaEspera toDomain(HistoricoListaEsperaEntity entity) {
		return new HistoricoListaEspera(entity.getId(), entity.getPacienteId(), entity.getConsultaId(),
				entity.getEspecialidade(), entity.getDescricao(), entity.getDataMovimentacao());
	}

	public static HistoricoListaEsperaEntity toEntity(HistoricoListaEspera historico) {
		HistoricoListaEsperaEntity entity = new HistoricoListaEsperaEntity();
		entity.setId(historico.id());
		entity.setPacienteId(historico.pacienteId());
		entity.setConsultaId(historico.consultaId());
		entity.setEspecialidade(historico.especialidade());
		entity.setDescricao(historico.descricao());
		entity.setDataMovimentacao(historico.dataMovimentacao());
		return entity;
	}

	public static Usuario toDomain(UsuarioEntity entity) {
		return new Usuario(entity.getId(), entity.getNome(), entity.getEmail(), entity.getSenhaHash(), entity.getPerfil());
	}

	public static UsuarioEntity toEntity(Usuario usuario) {
		UsuarioEntity entity = new UsuarioEntity();
		entity.setId(usuario.id());
		entity.setNome(usuario.nome());
		entity.setEmail(usuario.email());
		entity.setSenhaHash(usuario.senhaHash());
		entity.setPerfil(usuario.perfil());
		return entity;
	}
}
