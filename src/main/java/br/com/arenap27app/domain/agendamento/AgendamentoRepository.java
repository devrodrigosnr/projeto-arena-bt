package br.com.arenap27app.domain.agendamento;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.arenap27app.domain.aluno.Aluno;
import br.com.arenap27app.domain.aula.Aula;
import br.com.arenap27app.domain.enums.SituacaoAgendamento;
import br.com.arenap27app.domain.enums.SituacaoAula;
import br.com.arenap27app.domain.enums.SituacaoPresenca;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer>{
	
	boolean existsByAlunoAndAula(Aluno aluno, Aula aula);
	
	void deleteByAulaAndAluno(Aula aula, Aluno aluno);
	
	int countByAulaIdAndSituacaoAgendamento(Integer aulaId, SituacaoAgendamento situacao);

	Agendamento findByAlunoAndAulaAndSituacaoAgendamento(Aluno aluno, Aula aula, SituacaoAgendamento situacao);
	
    Long countByAulaIdAndAulaSituacaoAndSituacaoAgendamento(Long aulaId, SituacaoAula situacaoAula, SituacaoAgendamento situacaoAgendamento);
    
    int countByAlunoAndSituacaoPresencaAndAula_DataBetween(Aluno aluno, SituacaoPresenca situacaoPresenca, LocalDate inicio, LocalDate fim);



}
