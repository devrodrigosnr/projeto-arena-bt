package br.com.arenap27app.domain.agendamento;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.arenap27app.application.ValidationException;
import br.com.arenap27app.domain.aluno.Aluno;
import br.com.arenap27app.domain.aluno.AlunoService;
import br.com.arenap27app.domain.aula.Aula;
import br.com.arenap27app.domain.enums.SituacaoAgendamento;
import br.com.arenap27app.domain.enums.SituacaoAluno;
import br.com.arenap27app.domain.enums.SituacaoAlunoAulaExperimental;
import br.com.arenap27app.domain.enums.SituacaoPresenca;
import jakarta.transaction.Transactional;

@Service
public class AgendamentoService {
	
	@Autowired
	private AgendamentoRepository agendamentoRepository;
	
	@Autowired
	private AlunoService alunoService;

	@Transactional
	public void realizarCheckin(Agendamento agendamento) {
		agendamentoRepository.save(agendamento);	
		if(agendamento.getAluno().getSituacaoAluno().equals(SituacaoAluno.EXPERIMENTAL) 
				&& (agendamento.getSituacaoPresenca() == null || agendamento.getSituacaoPresenca().equals(SituacaoPresenca.PRESENTE))) {
			agendamento.getAluno().setSituacaoAlunoAulaExperimental(SituacaoAlunoAulaExperimental.COMPARECEU);
		} else if(agendamento.getAluno().getSituacaoAluno().equals(SituacaoAluno.EXPERIMENTAL) 
				&& agendamento.getSituacaoPresenca().equals(SituacaoPresenca.AUSENTE)) {
			agendamento.getAluno().setSituacaoAlunoAulaExperimental(SituacaoAlunoAulaExperimental.NAO_COMPARECEU);
			}
			try {
				alunoService.salvarAluno(agendamento.getAluno());
			} catch (ValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public boolean isAlunoAgendadoParaAula(Aluno aluno, Aula aula) {
		return agendamentoRepository.existsByAlunoAndAula(aluno, aula);
	}
	
	@Transactional
    public void desmarcarAgendamento(Agendamento agendamento) {
        agendamentoRepository.save(agendamento);
    }

	public boolean validarSeTemVagaDisponivel(Aula aula) {
		int qtdAgendamentos = agendamentoRepository.countByAulaIdAndSituacaoAgendamento(aula.getId(), SituacaoAgendamento.AGENDADO);
		return qtdAgendamentos >= aula.getVagas();
	}

	public Agendamento buscarAgendamentoPorAlunoAulaESituacao(Aluno aluno, Aula aula, SituacaoAgendamento situacao) {
		return agendamentoRepository.findByAlunoAndAulaAndSituacaoAgendamento(aluno, aula, situacao);
	}
	
	public Optional<Agendamento> validarAgendamentoRealizadoParaData(Aluno aluno, List<Aula> aulas) {
		return aulas.stream()
    	    .flatMap(aulaAgendada -> aulaAgendada.getAgendamentos().stream())
    	    // Filtra os agendamentos com situação 'AGENDADA' e id do aluno correspondente
    	    .filter(agendamentoRealizado -> 
    	    		(agendamentoRealizado.getSituacaoAgendamento().equals(SituacaoAgendamento.AGENDADO) 
    	    		|| agendamentoRealizado.getSituacaoAgendamento().equals(SituacaoAgendamento.PRESENTE))
    	            && agendamentoRealizado.getAluno().getId().equals(aluno.getId()))
    	    // Retorna o primeiro agendamento encontrado, se houver
    	    .findFirst();
	}
	
	public Integer verificarQuantidadeDeVagasDisponiveisParaAula(Aula aula) {
		return agendamentoRepository.countByAulaIdAndSituacaoAgendamento(aula.getId(), SituacaoAgendamento.AGENDADO);
	}
	
	public Integer verificaQuantidadeDeAulasRealizadasNoMes(Aluno aluno) {
		LocalDate dataInicio = LocalDate.now().withDayOfMonth(1);
		LocalDate dataFim = LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1);
		return agendamentoRepository.countByAlunoAndSituacaoPresencaAndAula_DataBetween(aluno, SituacaoPresenca.PRESENTE, dataInicio, dataFim);
	}
	
	public boolean isLimiteDeAulasNoMesAtingido(Aluno aluno) {
		int quantidadeAulasRealizadasMes = verificaQuantidadeDeAulasRealizadasNoMes(aluno);
		return quantidadeAulasRealizadasMes >= aluno.getPlano().getFrequenciaMaxima();
	}
	
	public void agendarAlunoFilaEspera(Agendamento agendamento) {
		agendamentoRepository.save(agendamento);
	}
}
